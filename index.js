'use strict'

const functions = require('firebase-functions');
const algoliasearch = require('algoliasearch');
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
// The Firebase Admin SDK to access the Firebase Realtime Database. 
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// [START init_algolia]
// Initialize Algolia, requires installing Algolia dependencies:
// https://www.algolia.com/doc/api-client/javascript/getting-started/#install
//
// App ID and API Key are stored in functions config variables
const ALGOLIA_ID = functions.config().algolia.app_id;
const ALGOLIA_ADMIN_KEY = functions.config().algolia.api_key;
const ALGOLIA_SEARCH_KEY = functions.config().algolia.search_key;

const ALGOLIA_INDEX_NAME = 'produk_reguler';
const client = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);
// [END init_algolia]


// [START update_index_function]
// Update the search index every time a blog post is written.
exports.onProdukCreated = functions.firestore.document('produk_reguler/{idProduk}').onCreate(event => {
    // Get the note document
    const produk = event.data.data();

    // Add an 'objectID' field which Algolia requires
    produk.objectID = event.params.idProduk;

    // Write to the algolia index
    const index = client.initIndex(ALGOLIA_INDEX_NAME);
    return index.saveObject(produk);
});
// [END update_index_function]

// [START get_firebase_user]
// const admin = require('firebase-admin');
// admin.initializeApp(functions.config().firebase);

function getFirebaseUser(req, res, next) {
    console.log('Check if request is authorized with Firebase ID token');

    if (!req.headers.authorization
        || !req.headers.authorization.startsWith('Bearer ')) {
        console.error(
            'No Firebase ID token was passed as a Bearer token in the Authorization header.',
            'Make sure you authorize your request by providing the following HTTP header:',
            'Authorization: Bearer <Firebase ID Token>'
        );
        res.status(403).send('Unauthorized');
        return;
    }

    let idToken;
    if (
        req.headers.authorization &&
        req.headers.authorization.startsWith('Bearer ')
    ) {
        console.log('Found \'Authorization\' header');
        idToken = req.headers.authorization.split('Bearer ')[1];
    }

    admin
        .auth()
        .verifyIdToken(idToken)
        .then(decodedIdToken => {
            console.log('ID Token correctly decoded', decodedIdToken);
            req.user = decodedIdToken;
            next();
        }).catch(error => {
            console.error('Error while verifying Firebase ID token:', error);
            res.status(403).send('Unauthorized');
        });
}
// [END get_firebase_user]

// [START get_algolia_user_token]
// This complex HTTP function will be created as an ExpressJS app:
// https://expressjs.com/en/4x/api.html
const app = require('express')();

// We'll enable CORS support to allow the function to be invoked
// from our app client-side.
app.use(require('cors')({ origin: true }));

// Then we'll also use a special 'getFirebaseUser' middleware which
// verifies the Authorization header and adds a `user` field to the
// incoming request:
// https://gist.github.com/abehaskins/832d6f8665454d0cd99ef08c229afb42
app.use(getFirebaseUser);

// Add a route handler to the app to generate the secured key
app.get('/', (req, res) => {
    // Create the params object as described in the Algolia documentation:
    // https://www.algolia.com/doc/guides/security/api-keys/#generating-api-keys
    const params = {
        // This filter ensures that only documents where author == user_id will be readable
        filters: `author:${req.user.user_id}`,
        // We also proxy the user_id as a unique token for this key.
        userToken: req.user.user_id
    };

    // Call the Algolia API to generate a unique key based on our search key
    const key = client.generateSecuredApiKey(ALGOLIA_SEARCH_KEY, params);

    // Then return this key as {key: '...key'}
    res.json({ key });
});

// Finally, pass our ExpressJS app to Cloud Functions as a function
// called 'getSearchKey';
exports.getSearchKey = functions.https.onRequest(app);
// [END get_algolia_user_token]



/*
 * 'OnWrite' works as 'addValueEventListener' for android. It will fire the function
 * everytime there is some item added, removed or changed from the provided 'database.ref'
 * 'sendNotification' is the name of the function, which can be changed according to
 * your requirement
 */
exports.sendNotifObrolanToPenjual = functions.database.ref('notifikasi/penjual/obrolan/{id_penerima}/{id_notifikasi}')
.onWrite(event =>{
    const id_penerima = event.params.id_penerima;
    const id_notifikasi = event.params.id_notifikasi;

    console.log('Notifikasi untuk : ', id_penerima);

    if(!event.data.val()){
        return console.log('notifikasi di hapus : ', id_notifikasi);
    }

    /*
    Query 'fromUser' mengambil ID pengguna yang mengirim notifikasi
    */
    const pengirim = admin.database().ref(`notifikasi/penjual/obrolan/${id_penerima}/${id_notifikasi}`).once('value');

    return pengirim.then(pengirimHasil => {

        const id_pengirim = pengirimHasil.val().pengirimId;

        console.log('Anda dapat notif baru dari : ', id_pengirim);

        const userQuery = admin.database().ref(`data_konsumen/${id_pengirim}/detailKonsumen/nama`).once('value');
        const device_token = admin.database().ref(`data_penjual/${id_penerima}/deviceToken`).once('value');
        const avatar = admin.database().ref(`data_konsumen/${id_pengirim}/detailKonsumen/avatar`).once('value');

        return Promise.all([userQuery, device_token, avatar]).then(result => {

            const userName = result[0].val();
            const token_id = result[1].val();
            const userAvatar = result[2].val();

            const payload = {
                notification:{
                    title : "Obrolan baru",
                    body : `${userName} mengirimkan pesan baru`,
                    icon: "default",
                    click_action : "com.example.aryaym.mlijopenjual_Notifikasi_Obrolan"
                },
                data : {
                    id_pengirim : id_pengirim,
                    userAvatar : userAvatar,
                    userName : userName
                }
            };

            //Create an options object that contains the time to live for the notification and the priority
            const options = {
                priority: "high",
                timeToLive: 60 * 60 * 2
            };

            return admin.messaging().sendToDevice(token_id, payload, options).then(Response => {

                console.log('this notification succes');
            });
        });
    });
});

/**
 * notif to konsumen
 */
exports.sendNotifObrolanToKonsumen = functions.database.ref('notifikasi/konsumen/obrolan/{id_penerima}/{id_notifikasi}')
.onWrite(event =>{
    const id_penerima = event.params.id_penerima;
    const id_notifikasi = event.params.id_notifikasi;

    console.log('Notifikasi untuk : ', id_penerima);

    if(!event.data.val()){
        return console.log('notifikasi di hapus : ', id_notifikasi);
    }

    /*
    Query 'fromUser' mengambil ID pengguna yang mengirim notifikasi
    */
    const pengirim = admin.database().ref(`notifikasi/konsumen/obrolan/${id_penerima}/${id_notifikasi}`).once('value');

    return pengirim.then(pengirimHasil => {

        const id_pengirim = pengirimHasil.val().pengirimId;

        console.log('Anda dapat notif baru dari : ', id_pengirim);

        const userQuery = admin.database().ref(`data_penjual/${id_pengirim}/detailPenjual/nama`).once('value');
        const device_token = admin.database().ref(`data_konsumen/${id_penerima}/deviceToken`).once('value');
        const avatar = admin.database().ref(`data_penjual/${id_pengirim}/detailPenjual/avatar`).once('value');

        return Promise.all([userQuery, device_token, avatar]).then(result => {

            const userName = result[0].val();
            const token_id = result[1].val();
            const userAvatar = result[2].val();

            const payload = {
                notification:{
                    title : "Obrolan baru",
                    body : `${userName} mengirimkan pesan baru`,
                    icon: "default",
                    click_action : "com.example.aryaym.mlijokonsumen_Notifikasi_Obrolan"
                },
                data : {
                    id_pengirim : id_pengirim,
                    userAvatar : userAvatar,
                    userName : userName
                }
            };

            //Create an options object that contains the time to live for the notification and the priority
            const options = {
                priority: "high",
                timeToLive: 60 * 60 * 2
            };

            return admin.messaging().sendToDevice(token_id, payload, options).then(Response => {

                console.log('this notification succes');
            });
        });
    });
});
/**
 * order to penjual
 */
exports.sendNotifOrderToPenjual = functions.database.ref('notifikasi/penjual/order/{id_penjual}/{id_notifikasi}')
.onWrite(event =>{
    const id_penjual = event.params.id_penjual;
    const id_notifikasi = event.params.id_notifikasi;

    console.log('Notifikasi untuk : ', id_penjual);

    if(!event.data.val()){
        return console.log('notifikasi di hapus dari: ', id_notifikasi);
    }

    /*
    Query 'fromUser' mengambil ID pengguna yang mengirim notifikasi
    */
    const konsumen = admin.database().ref(`notifikasi/penjual/order/${id_penjual}/${id_notifikasi}`).once('value');

    return konsumen.then(konsumenHasil => {

        const id_konsumen = konsumenHasil.val().konsumenId;
        /*
        const title_order = konsumenHasil.val().title;
        const transaksi_order = konsumenHasil.val().transaksi;*/

        console.log('Anda dapat notif baru dari : ', id_konsumen);

        const userQuery = admin.database().ref(`data_konsumen/${id_konsumen}/detailKonsumen/nama`).once('value');
        const device_token = admin.database().ref(`data_penjual/${id_penjual}/deviceToken`).once('value');
        const titleQuery = admin.database().ref(`notifikasi/penjual/order/${id_penjual}/${id_notifikasi}/title`).once('value');
        const transaksiQuery = admin.database().ref(`notifikasi/penjual/order/${id_penjual}/${id_notifikasi}/transaksi`).once('value');

        return Promise.all([userQuery, device_token, titleQuery, transaksiQuery]).then(result => {

            const userName = result[0].val();
            const token_id = result[1].val();
            const title_order = result[2].val();
            const transaksi_order = result[3].val();

            const payload = {
                notification:{
                    title : "Pesanan Baru",
                    body : `${userName} melakukan pemesanan produk`,
                    icon: "default",
                    click_action : "com.example.aryaym.mlijopenjual_Notifikasi_Order"
                },
                data : {
                    id_konsumen : id_konsumen,
                    title_order : title_order,
                    transaksi_order : transaksi_order
                }
            };

            //Create an options object that contains the time to live for the notification and the priority
            const options = {
                priority: "high",
                timeToLive: 60 * 60 * 2
            };

            return admin.messaging().sendToDevice(token_id, payload, options).then(Response => {

                console.log('this notification succes');
            });
        });
    });
});

exports.sendNotifTerimaOrderToKonsumen = functions.database.ref('notifikasi/konsumen/order/{id_konsumen}/{id_notifikasi}')
.onWrite(event =>{
    const id_konsumen = event.params.id_konsumen;
    const id_notifikasi = event.params.id_notifikasi;

    console.log('Notifikasi untuk : ', id_konsumen);

    if(!event.data.val()){
        return console.log('notifikasi di hapus dari: ', id_notifikasi);
    }


    const penjual = admin.database().ref(`notifikasi/konsumen/order/${id_konsumen}/${id_notifikasi}`).once('value');
    
        return penjual.then(penjualHasil => {
    
            const id_penjual = penjualHasil.val().penjualId;
            const test = penjualHasil.val().cobaint;
            /*
            const title_order = konsumenHasil.val().title;
            const transaksi_order = konsumenHasil.val().transaksi;*/
    
            console.log('Anda dapat notif baru dari : ', id_penjual);
    
            const userQuery = admin.database().ref(`data_penjual/${id_penjual}/detailPenjual/nama`).once('value');
            const device_token = admin.database().ref(`data_konsumen/${id_konsumen}/deviceToken`).once('value');
            const titleQuery = admin.database().ref(`notifikasi/konsumen/order/${id_konsumen}/${id_notifikasi}/title`).once('value');
            const transaksiQuery = admin.database().ref(`notifikasi/konsumen/order/${id_konsumen}/${id_notifikasi}/transaksi`).once('value');
    
            return Promise.all([userQuery, device_token, titleQuery, transaksiQuery]).then(result => {
    
                const userName = result[0].val();
                const token_id = result[1].val();
                const title_order = result[2].val();
                const transaksi_order = result[3].val();
    
                const payload = {
                    notification:{
                        title : "Update status pesanan",
                        body : `Pemesanan anda telah di konfirmasi oleh ${userName} `,
                        icon: "default",
                        sound:"default",
                        click_action : "com.example.aryaym.mlijopenjual_Notifikasi_Order"
                    },
                    data : {
                        id_konsumen : id_konsumen,
                        title_order : `${test}`,
                        transaksi_order : transaksi_order
                    }
                };

                //Create an options object that contains the time to live for the notification and the priority
                const options = {
                    priority: "high",
                    timeToLive: 60 * 60 * 2
                };
    
                return admin.messaging().sendToDevice(token_id, payload, options).then(Response => {
    
                    console.log('this notification succes');
                });
            });
        });

});