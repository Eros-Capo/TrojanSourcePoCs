import json

# read value from utf8 json file
with open('data.json', encoding='utf8') as f:
    data = json.load(f)



print(data["Server"]["Host"])
print(data["Server"]["Нost"])


