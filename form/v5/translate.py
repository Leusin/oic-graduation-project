import json
import urllib.request

def papagoAPI(inputKeyword):
    client_id = "I19UAaH8cIC00uw9Z7W_"
    client_secret = "1VTYCR65Hy"

    encText = urllib.parse.quote("{}".format(inputKeyword))
    print(inputKeyword)

    data = "source=en&target=ko&text=" + encText
    url = "https://openapi.naver.com/v1/papago/n2mt"

    papago_api_request = urllib.request.Request(url)
    papago_api_request.add_header("X-Naver-Client-Id",client_id)
    papago_api_request.add_header("X-Naver-Client-Secret",client_secret)
    response = urllib.request.urlopen(papago_api_request, data=data.encode("utf-8"))
    rescode = response.getcode()
    if(rescode==200):
        response_body = response.read()
        json_data = json.loads(response_body)
        #print('json_data = ', json_data.get('json_data'))
        #print('json_data = ', json_data['message']['result']['translatedText'] )
        return json_data['message']['result']['translatedText']