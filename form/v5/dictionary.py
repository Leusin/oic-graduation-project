import json
import urllib.request

def freeDictionaryAPI(inputKeyword):

    encText = urllib.parse.quote("{}".format(inputKeyword))
    url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+encText

    dictionary_api_request = urllib.request.Request(url)
    response = urllib.request.urlopen(dictionary_api_request)
    rescode = response.getcode()
    if(rescode==200):
        response_body = response.read()
        json_data = json.loads(response_body)
        #print(json.dumps(json_data, indent="\t"))
        audio = '{}'.format(json_data[0]['phonetics'][1]['audio'])
        definition = '{}'.format(json_data[0]['meanings'][1]['definitions'])
        print("definition")
        print(definition)
        return (audio, definition)
        #print(json_data['meanings']['defintions'])


"""
def freeDictionary2API(inputKeyword):

    encText = urllib.parse.quote("{}".format(inputKeyword))
    print(encText)
    url = "https://api.dictionaryapi.dev/api/v2/entries/en/"+encText
    print(url)

    dictionary_api_request = urllib.request.Request(url)
    response = urllib.request.urlopen(dictionary_api_request)
    rescode = response.getcode()
    if(rescode==200):
        response_body = response.read()
        json_data = json.loads(response_body)
        print(json.dumps(json_data, indent="\t"))
        return '{}'.format(json_data[0]['meanings'][1]['definitions'])
        #print(json_data['meanings']['defintions'])"""