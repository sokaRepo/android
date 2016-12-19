# Mobile App API

### Database content

*users table: contains the users information*
```sql
CREATE TABLE users (
id integer key autoincrement,
username text,
password text,
email text)
```
*users_settings table: contains the users settings*
```sql
CREATE TABLE users_settings(
user_id integer,
user_settings text,
foreign key(user_id) references users(id))
```

### API functionality

* Create new user<br>
*Insert new user in database with username, [sha1]password and email*

```
 POST /api/register
 
 {"username":"toto", "password":"0b9c2625dc21ef05f6ad4ddf47c5f203837aa32c", "email":"toto@toto.com"}
```
Server response if successful registration
```
{"result": true}
```

* Generate token for user<br>
*Generate a secure token for user, it will be used to process API actions*

```
GET /api/generate-token/username/password

 {"username":"toto","password":"0b9c2625dc21ef05f6ad4ddf47c5f203837aa32c"}
```
Server response if credentials are corrects
```
{"token": "e6439969ca355b3c45362e7c37828240d688e7ee", "result": true}
```

* Show user settings
```
POST /api/show/settings

{"token":"e6439969ca355b3c45362e7c37828240d688e7ee"}
```
Server response if token is valid
```
{"data": {"tel": "0656784512", "clock": "00-07"}, "result": true}
```
* Update user settings
```
POST /api/set/settings

{"token":"e6439969ca355b3c45362e7c37828240d688e7ee", "data": {"clock":"00-07", "tel":"0656784512|tel2|tel3"}}
```
Server response if everything went great
```
{"result": true}
```