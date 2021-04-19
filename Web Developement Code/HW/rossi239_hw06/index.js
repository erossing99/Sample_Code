// YOU CAN USE THIS FILE AS REFERENCE FOR SERVER DEVELOPMENT
const createError = require('http-errors');

// Include the express module
const express = require('express');

// helps in extracting the body portion of an incoming request stream
var bodyparser = require('body-parser');

// Path module - provides utilities for working with file and directory paths.
const path = require('path');

// Helps in managing user sessions
const session = require('express-session');

// include the mysql module
var mysql = require('mysql');

// Bcrypt library for comparing password hashes
const bcrypt = require('bcrypt');

// Include the express router.
const utilities = require('./api/utilities');

const port = 9130; //student id = 5441130

// create an express application
const app = express();

// Use express-session
// In-memory session is sufficient for this assignment
app.use(session({
        secret: "csci4131secretkey",
        saveUninitialized: true,
        resave: false
    }
));

// middle ware to serve static files
app.use(express.static(path.join(__dirname, 'public')));

app.use(express.json());
app.use(express.urlencoded());

//setup sql connection
const connection = mysql.createConnection({
    host: "cse-mysql-classes-01.cse.umn.edu",
    user: "C4131S21U75",               // replace with the database user provided to you
    password: "5655",                  // replace with the database password provided to you
    database: "C4131S21U75",           // replace with the database user provided to you
    port: 3306
});

connection.connect(function(err){
  if(err){
    throw err;
  };
  console.log("Conncected to MySQL database.");
});

// server listens on port 9002 for incoming connections
app.listen(port, () => console.log('Listening on port', port));

app.get('/', function (req, res) {
    console.log("Starting Session");
    res.sendFile(path.join(__dirname, 'public/welcome.html'));
});

app.get('/login', function(req, res) {
    res.sendFile(path.join(__dirname, 'client/login.html'));
});

app.get('/logout', function(req, res) {
    req.session.destroy();
    res.sendFile(path.join(__dirname, 'client/login.html'));
});

app.post('/checkLogin', function(req, res) {
    //TODO add login functionality
    //console.log("in checkLogin");
    var loginInfo = req.body;
    //console.log(loginInfo);
    var login = loginInfo.login;
    //const saltRounds = 10;
    var pwd = loginInfo.password;
    // console.log("Login is: " + login);
    // console.log("Pwd is: " + pwd);
    //query the database tbl_login with login and hashed password
    connection.query("SELECT * FROM tbl_accounts WHERE acc_login = " + mysql.escape(login), function(err, rows, fields){
      if (err) throw err;
      //provided there is no error, and the results set is assigned to a variabled named rows:
      if (rows.length >= 1){
        var hashedPassword = rows[0].acc_password;
        bcrypt.compare(pwd, hashedPassword, function(err, result){
          if (result){
            req.session.user = login;
            req.session.value = 1;
            res.json({status: 'success'});
          }
          else{
            res.json({status: 'fail'});
          }
        });
      }
      else{
        res.json({status: 'fail'});
      }
    });
});

// GET method route for the contacts page.
// It serves contact.html present in public folder
app.get('/contacts', function(req, res) {
    if(!req.session.value){
      res.sendFile(path.join(__dirname, 'client/login.html'));
    }
    else{
      res.sendFile(path.join(__dirname, 'client/contacts.html'));
    }
});

app.get('/contactTable', function(req, res) {
    connection.query("SELECT * FROM tbl_contacts", function(err, rows, fields){
      if (err) throw err;
      res.send(rows);
    });
});

// TODO: Add implementation for other necessary end-points

app.get('/addContact', function(req, res) {
    if(!req.session.value){
      res.sendFile(path.join(__dirname, 'client/login.html'));
    }
    else{
      res.sendFile(path.join(__dirname, 'client/addContact.html'));
    }
});

app.post('/postContactEntry', function(req, res) {
  var postObj = req.body;
  var name = postObj.name;
  var category = postObj.category;
  var location = postObj.location;
  var contact = postObj.contact;
  var email = postObj.email;
  var website_name = postObj.website_name;
  var website_url = postObj.website_url;
  var rowToBeInserted = {
    name: name,
    category: category,
    location: location,
    contact_info: contact,
    email: email,
    website: website_name,
    website_url: website_url
  };
  connection.query('INSERT tbl_contacts SET ?', rowToBeInserted, function(err, result){
    if (err) throw err;
    res.sendFile(path.join(__dirname, 'client/contacts.html'));
  });
});

app.get('/welcome', function(req, res) {
    res.sendFile(path.join(__dirname, 'public/welcome.html'));
});

app.get('/stock', function(req, res) {
    if(!req.session.value){
      res.sendFile(path.join(__dirname, 'client/login.html'));
    }
    else{
      res.sendFile(path.join(__dirname, 'client/stock.html'));
    }
});

app.get('*', function(req, res) {
    res.sendStatus(404);
});

// Makes Express use a router called utilities
app.use('/api', utilities);

// function to return the 404 message and error to client
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    // res.render('error');
    res.send();
});
