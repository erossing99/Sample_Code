
const http = require('http');
const url = require('url');
const fs = require('fs');
const qs = require('querystring');

const port = 9239;
http.createServer(function (req, res) {
  var q = url.parse(req.url, true);
  var filename = "." + q.pathname;
  if(req.url === '/'){
    indexPage(req,res);
  }
  else if(req.url === '/index.html'){
    indexPage(req,res);
  }
  else if(req.url === '/contacts.html'){
    contactPage(req,res);
  }
  else if(req.url === '/addContact.html'){
    addContactPage(req,res);
  }
  else if(req.url === '/contacts.json'){
    contactsJson(req,res);
  }
  else if(req.url === '/stock.html'){
    stockPage(req,res);
  }
  else if(req.url === '/postContactEntry'){
    var reqBody = '';
    req.on('data', function(data){
      reqBody += data;
    });
    req.on('end', function(){
      postContact(reqBody,res,req);
    });
  }
  else{
    res.writeHead(404, {'Content-Type': 'text/html'});
    return res.end("404 Not Found");
  }
}).listen(port);


function indexPage(req, res) {
  fs.readFile('client/index.html', function(err, html) {
    if(err) {
      throw err;
    }
    res.statusCode = 200;
    res.setHeader('Content-type', 'text/html');
    res.write(html);
    res.end();
  });
}


function contactPage(req, res) {
  fs.readFile('client/contacts.html', function(err, html) {
    if(err) {
      throw err;
    }
    res.statusCode = 200;
    res.setHeader('Content-type', 'text/html');
    res.write(html);
    res.end();
  });
}

function addContactPage(req, res) {
  fs.readFile('client/addContact.html', function(err, html) {
    if(err) {
      throw err;
    }
    res.statusCode = 200;
    res.setHeader('Content-type', 'text/html');
    res.write(html);
    res.end();
  });
}

function stockPage(req, res) {
  fs.readFile('client/stock.html', function(err, html) {
    if(err) {
      throw err;
    }
    res.statusCode = 200;
    res.setHeader('Content-type', 'text/html');
    res.write(html);
    res.end();
  });
}

function contactsJson(req, res) {
  fs.readFile('contacts.json', function(err, contactsJSON) {
    if(err) {
      throw err;
    }
    res.statusCode = 200;
    res.setHeader('Content-type', 'text/json');
    res.write(contactsJSON);
    res.end();
  });
}

function postContact(reqBody,res,req){
  //TODO:
  //read the posted data
  //add new info to contacts.json
  //redirect the file contacts.html
  var postObj = qs.parse(reqBody);
  var name = postObj.name;
  var category = postObj.category;
  var location = postObj.location;
  var contact = postObj.contact;
  var email = postObj.email;
  var website_name = postObj.website_name;
  var website_url = postObj.website_url;
  var jsonObj = {};
  jsonObj['name'] = name;
  jsonObj['category'] = category;
  jsonObj['location'] = location;
  jsonObj['contact'] = contact;
  jsonObj['email'] = email;
  jsonObj['website_name'] = website_name;
  jsonObj['website_url'] = website_url;
  fs.readFile('contacts.json', function(err, contactsJSON) {
    if(err) {
      throw err;
    }
    var fileJson = JSON.parse(contactsJSON);
    fileJson.contacts.push(jsonObj);
    var strJson = JSON.stringify(fileJson);
    fs.writeFile('contacts.json', strJson, function(err){
      if (err) {
        throw err;
      }
    });
    // res.writeHead(302,{"Location":"/contacts.html"});
    // res.end();
  });
  res.writeHead(302,{"Location":"/contacts.html"});
  res.end();
}
