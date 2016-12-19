#-*- coding:utf8 -*-
from flask import Flask, render_template, _app_ctx_stack, request, session, redirect, url_for, jsonify
from utils import *



app = Flask(__name__)

app.config['SECRET_KEY'] = 'secret!'
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 60

# import routes
app.register_blueprint(api)



@app.teardown_appcontext
def close_database(exception):
    """Closes the database again at the end of the request."""
    top = _app_ctx_stack.top
    if hasattr(top, 'sqlite_db'):
    	top.sqlite_db.close()


@app.route('/')
def index():
	return render_template('json.html', content='Mobile API')


@app.route('/test', methods=['POST'])
def test():
	data = request.get_json(force=True)
	print data
	if login(data):
		return render_template('json.html', content=json.dumps({'result':'True'}))
	return render_template('json.html', content=json.dumps({'result':'False'}))

if __name__ == '__main__':
	app.run(port=5000, debug=True)
