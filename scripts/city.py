import sqlite3
import json
import os

os.remove("Nabu.db")
create = "CREATE TABLE weather (id INTEGER PRIMARY KEY, city text, country text);"
format_string = "INSERT INTO weather VALUES ({},\"{}\",\"{}\");"

with open("city.list.json") as file:
	data = json.load(file)

conn = sqlite3.connect('Nabu.db')
c = conn.cursor()
c.execute(create)
conn.commit()

for i in data:
	c.execute(format_string.format(int(i['id']), i['name'].replace("\"", "'"), i['country']))

conn.commit()
c.execute("SELECT COUNT(city) FROM weather")
reply = c.fetchone()[0]
print( "Tabela criada e inserção completa" if reply == len(data) else "Tabela criada mas erro na inserção")
