#!/usr/bin/env python3
# See https://docs.python.org/3.2/library/socket.html
# for a decscription of python socket and its parameters

import socket
#add the following modules, so we can refactor EchoServer into HTTP HEAD Server
import os
import stat
import sys
import urllib.parse
import datetime

from threading import Thread
from argparse import ArgumentParser

BUFSIZE = 4096
#add the following
CRLF = '\r\n'
METHOD_NOT_ALLOWED = 'HTTP/1.1 405  METHOD NOT ALLOWED{}Allow: GET, HEAD, POST {}Connection: close{}{}'.format(CRLF, CRLF, CRLF, CRLF)
OK = 'HTTP/1.1 200 OK{}{}{}'.format(CRLF, CRLF, CRLF) # head request only
NOT_FOUND = 'HTTP/1.1 404 NOT FOUND{}Connection: close{}{}'.format(CRLF, CRLF, CRLF)
FORBIDDEN = 'HTTP/1.1 403 FORBIDDEN{}Connection: close{}{}'.format(CRLF, CRLF, CRLF)

# head requests, get contents of text or html files
def get_contents(fname):
    with open(fname, 'r') as f:
        return f.read()

# check file permissions -is file world readable?
def check_perms(resource):
    #Returns True if resource has read permissions set on 'others'
    stmode = os.stat(resource).st_mode
    return (getattr(stat, 'S_IROTH') & stmode) > 0

class HTTP_Server:
    def __init__(self, host, port):
        print("Server")
        print('listening on port {}'.format(port))
        self.host = host
        self.port = port

        self.setup_socket()

        self.accept()

        self.sock.shutdown()
        self.sock.close()

    def setup_socket(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.bind((self.host, self.port))
        self.sock.listen(128)

    def accept(self):
        while True:
            (client, address) = self.sock.accept()
            th = Thread(target=self.accept_request, args=(client, address))
            th.start()

    # here, we add a function belonging to the class HTTP_Server to accept
    # and process a request
    def accept_request(self, client_sock, client_addr):
        print("accept request")
        data = client_sock.recv(BUFSIZE)
        req = data.decode('utf-8') #returns a string
        response=self.process_request(req) #returns a string
        #once we get a response, we chop it into utf encoded bytes
        #and send it (like EchoClien
        client_sock.send(response)

        #clean up the connection to the client
        #but leave the server socket for recieving requests open
        client_sock.shutdown(1)
        client_sock.close()

    #added method to process requests
    def process_request(self, request):
        print('######\nREQUEST:\n{}######'.format(request)) #log request in STDOUT
        linelist = request.strip().split(CRLF)
        reqline = linelist[0]
        rlwords = reqline.split() #retrieve request type
        if len(rlwords) == 0:
            return ''

        if rlwords[0] == 'HEAD':
            resource = rlwords[1][1:] # skip beginning /
            return self.head_request(resource)
        elif rlwords[0] == 'GET':
            resource = rlwords[1][1:]
            if "redirect" in resource: #check if redirect GET request
                return self.redirect(resource)
            return self.get_request(resource)
        elif rlwords[0] == 'POST':
            resource = linelist[len(linelist)-1] #access last line of request for form parameters
            return self.post_request(resource)
        else: #return 405 error
            response = METHOD_NOT_ALLOWED + "\r\n\r\n"
            response = response.encode()
            return response

    def head_request(self, resource):
        path = os.path.join('.', resource) #look in directory where server is running
        if not os.path.exists(resource):
            ret = NOT_FOUND
            ret = ret.encode()
        elif not check_perms(resource):
            ret = FORBIDDEN
            ret = ret.encode()
        else:
            ret = OK
            ret = ret.encode()
        return ret

    def redirect(self, resource):
        resource = resource.split("=") #split string to access query
        query = resource[1]
        header = 'HTTP/1.1 307 TEMPORARY REDIRECT\r\nLocation: '
        youtubeURL = 'https://www.youtube.com/results?search_query=' + query
        response = header + youtubeURL
        response = response.encode()
        return response

    def post_request(self, resource):
        #retrieve form information and format correctly
        resource = resource.split('&')
        name = resource[0]
        name = name.split('=')
        name = name[1]
        name = name.replace('+',' ')
        category = resource[1]
        category = category.split('=')
        category = category[1]
        location = resource[2]
        location = location.split('=')
        location = location[1]
        location = location.replace('+',' ')
        contact = resource[3]
        contact = contact.split('=')
        contact = contact[1]
        contact = contact.replace('+',' ')
        email = resource[4]
        email = email.split('=')
        email = email[1]
        email = email.replace('%40','@')
        website = resource[5]
        website = website.split('=')
        website = website[1]
        website = website.replace('%3A',':')
        website = website.replace('%2F','/')
        #build html file for form
        htmlString = '<!DOCTYPE html> <html lang="en"> <head> <meta charset="utf-8"> <title>My Contacts</title> <style> table,td,tr{border: 1px solid black;} table{border-collapse: collapse;} td{padding:4px;}</style> </head>'
        htmlString = htmlString + '<h3>This Form Data Submitted Successfully</h3> <table>'
        htmlString = htmlString + '<tr> <td>name</td> <td>' + name + '</td> </tr>'
        htmlString = htmlString + '<tr> <td>category</td> <td>' + category + '</td> </tr>'
        htmlString = htmlString + '<tr> <td>location</td> <td>' + location + '</td> </tr>'
        htmlString = htmlString + '<tr> <td>contact</td> <td>' + contact + '</td> </tr>'
        htmlString = htmlString + '<tr> <td>email</td> <td>' + email + '</td> </tr>'
        htmlString = htmlString + '<tr> <td>website</td> <td>' + website + '</td> </tr> </table>'
        header = 'HTTP/1.1 200 OK\r\n'
        header += 'Content-Type: text/html\r\n\r\n'
        header += htmlString
        header = header.encode()
        return header

    def get_request(self, resource):
        print(resource)
        path = os.path.join('.', resource)
        if not os.path.exists(resource): #check to make sure file exists
            f = open('404.html','r')
            fileContents = f.read()
            fileContents = fileContents.encode()
            ret = NOT_FOUND + "\r\n\r\n"
            ret = ret.encode()
            ret = ret + fileContents
        elif not check_perms(resource): #check the file has correct permissions
            f = open('403.html','r')
            fileContents = f.read()
            fileContents = fileContents.encode()
            ret = FORBIDDEN + "\r\n\r\n"
            ret = ret.encode()
            ret = ret + fileContents
        else:
            return self.get_file(path) #proceed to process GET request
        return ret

    def get_file(self, path):
        header = 'HTTP/1.1 200 OK\r\n'
        #determine file type
        if '.html' in path:
            header += 'Content-Type: text/html\r\n\r\n'
            f = open(path,'r')
            fileContents = f.read()
            fileContents = fileContents.encode()
            header = header.encode()
            result = header + fileContents
            return result
        elif '.png' in path:
            header += 'Content-Type: image/png\r\n\r\n'
            fileContents = self.get_media_contents(path)
            header = header.encode()
            result = header + fileContents
            return result
        elif '.jpg' in path:
            header += 'Content-Type: image/jpg\r\n\r\n'
            fileContents = self.get_media_contents(path)
            header = header.encode()
            result = header + fileContents
            return result
        elif '.mp3' in path:
            header += 'Content-Type: audio/mp3\r\n\r\n'
            fileContents = self.get_media_contents(path)
            header = header.encode()
            result = header + fileContents
            return result


    def get_media_contents(self, fname):
        with open(fname, 'rb') as f:
            return f.read()


def parse_args():
    parser = ArgumentParser()
    parser.add_argument('--host', type=str, default='localhost',help='specify a host to operate on (default: localhost)')
    parser.add_argument('-p', '--port', type=int, default=9001,help='specify a port to operate on (default: 9001)')
    args = parser.parse_args()
    return (args.host, args.port)

if __name__ == '__main__':
    (host, port) = parse_args()
    HTTP_Server(host, port)
