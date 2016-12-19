from flask import _app_ctx_stack, session
from sqlite3 import dbapi2 as sqlite3
from os import urandom
from api import api
import json

"""
Database functions and other utils functions
"""

def func():
	return True

def get_db():
	"""Opens a new database connection if there is none yet for the
	current application context.
	"""
	top = _app_ctx_stack.top
	if not hasattr(top, 'sqlite_db'):
		top.sqlite_db = sqlite3.connect('database.sqlite')
		top.sqlite_db.row_factory = sqlite3.Row
		return top.sqlite_db
	return top.sqlite_db


def query_db(query, args=(), one=False):
	"""Queries the database and returns a list of dictionaries."""
	cur = get_db().execute(query, args)
	rv = cur.fetchall()
	return (rv[0] if rv else None) if one else rv

def row_exists(db, table, row, id):
	""" Check if a submited row exists in given table """
	q = db.execute("select id from %s where %s = ?" % (table,row) , [id])
	res = q.fetchall()
	return True if res else False


def insert(table, fields=[], values=[]):
    # g.db is the database connection
    db = get_db()
    query = 'INSERT INTO %s (%s) VALUES (%s)' % (
        table,
        ', '.join(fields),
        ', '.join(['?'] * len(values))
    )
    db.execute(query, values)
    db.commit()
    db.close()

def update(table, condition, fields=[], values=[]):
    # g.db is the database connection
    db = get_db()
    query = 'UPDATE {} SET {} WHERE {}'.format(
    	table,
    	', '.join('{}=?'.format(el) for el in fields),
    	condition
    )
    db.execute(query, values)
    db.commit()
    db.close()

def login(data):
	""" data type: json {"username":"toto", "password":"superpassword"}"""
	username = data['username']
	password = data['password']
	req = query_db("select * from users where username = ? and password = ?", [username, password])
	return True if len(req) == 1 else False

def register(data):
	db = get_db()
	if row_exists(db, 'users', 'username', data['username']):
		return {"result":False, "msg":"Username already taken"}
	db.execute("INSERT INTO users (username, password, email) VALUES (?,?,?)", [data['username'],\
		data['password'], data['email']])
	db.commit()
	return {"result":True}


def get_user_settings(token):
	try:
		q = get_db().execute("SELECT user_settings FROM users_settings WHERE user_id = (SELECT id FROM users WHERE token = ?) ", [token])
		data = q.fetchall()
		if len(data) > 0:
			return {"result":True, "data":json.loads(data[0][0])}
		return {"result":False, "msg":"No settings"}
	except sqlite3.Error as e:
		print e
		return {"result":False, "msg":e}


def set_user_settings(token, settings):
	try:
		db = get_db()
		q = db.execute("UPDATE users_settings SET user_settings = ? WHERE user_id = (SELECT id FROM users WHERE token = ?)", [(settings), token])
		db.commit()
		return {"result": True}
	except sqlite3.Error as e:
		print e
		return {"result":False, "msg":"{}".format(e)}

def random_token():
	return urandom(20).encode('hex')