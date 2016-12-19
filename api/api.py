from flask import Blueprint, render_template, request, redirect, url_for
import utils
from json import dumps as jsonify
from json import loads



api = Blueprint('api', __name__)



@api.route('/api/generate-token/<username>/<password>', methods=['GET'])
def generate_token(username, password):
	"""Generate a secure token for the user"""
	data = {"username":username, "password":password}
	if not utils.login(data):
		return render_template('json.html', content=jsonify({"result":False}))
	token = utils.random_token()
	db = utils.get_db()
	while utils.row_exists(db, 'users', 'token', token):
		token = utils.random_token()
	utils.update('users', 'username = ? and password = ?', ['token'], [token, username, password])
	return render_template('json.html', content=jsonify({"result":True, "token": token}))

@api.route('/api/show-users', methods=['GET'])
def show_users():
	return render_template('users.json')


@api.route('/api/register', methods=['POST'])
def user_register():
	user_info = request.get_json(force=True)
	if not 'username' in user_info or not 'password' in user_info or not 'email' in user_info:
		return render_template('json.html', content=jsonify({"result":False, "msg":"Missing parameters"}))
	return render_template('json.html', content=jsonify(utils.register(user_info)))

@api.route('/api/show/settings', methods=['POST'])
def show_settings():
	data = request.get_json(force=True)
	if not 'token' in data:
		return render_template('json.html', jsonify({"result":False, "msg":"Missing parameter"}))
	settings = utils.get_user_settings(data['token'])
	return render_template('json.html', content=jsonify(settings))
	
@api.route('/api/set/settings', methods=['POST'])
def set_settings():
	data = request.get_json(force=True)
	if not 'token' in data or not 'data' in data:
		return render_template('json.html', content=jsonify({"result":False, "msg":"Missing parameter"}))
	if not utils.row_exists(utils.get_db(), 'users', 'token', data['token']):
		return render_template('json.html', content=jsonify({"result":False, "msg":"Invalid token"}))
	settings = data['data']
	if not 'clock' in settings or not 'tel' in settings:
		return render_template('json.html', content=jsonify({"result":False, "msg":"Invalid settings"}))
	r = utils.set_user_settings(str(data['token']),  jsonify(data['data']))
	return render_template('json.html', content=jsonify(r) )

@api.route('/api/test')
def api_test():
	if func():
		return render_template('json.html', content='test')
	return render_template('json.html', content='test')