import requests, json, os, time

if not os.path.isfile('users.json') or time.time() - 3600 > os.path.getmtime('users.json'):
  data = requests.get('https://exostats.nl/?api&users').text
  f = open('users.json', 'w')
  f.write(data)
  f.close()
data = json.load(open('users.json', 'r'))

players = dict()
fielddata = data['fields']
playerdata = data['data']
for playerarray in playerdata:
  playerobj = dict()
  for index, field in enumerate(fielddata):
    playerobj[field] = playerarray[index]
  userid = playerobj['userid']
  players[userid] = playerobj

playerscoredata = requests.get('https://exostats.nl/?rank=playerscore&asJson').json()
for playerid in playerscoredata:
    if playerid in players:
        players[playerid]['score'] = playerscoredata[playerid]
    else:
        # Handle the case where 'playerid' is not found in the 'players' dictionary
        print(f"Player with ID {playerid} not found in the 'players' dictionary.")

memberScore = dict()
#chosenClub = '6Cx6agaCkcy8U1Qo1PYw' beach
#chosenClub = '5r3hIXmkuvu5qE7ryrlK' im bad
#chosenClub = 'cRIUTJDxIE139dLzZYJl' the shrine
chosenClub = 'cRIUTJDxIE139dLzZYJl'

for player in players:
    player = players[player]
    if player['clubId'] not in (None, ' ', False):
        clubId = player['clubId']
        if 'score' in player and clubId == chosenClub:
          memberScore[player['score']['name']] = player['score']['score']

memberScore = sorted(memberScore.items(), key=lambda x: x[1], reverse=True)
total = 0
memCount = 0
for member, score in memberScore:
    total += score
    memCount += 1
    print (member.ljust(15),round(score,3))

print ('total:', round(total,3), 'average:', round(total/memCount,3)) 

clubScore = dict()
clubMembers = dict()
clubIndividuals = dict()
for player in players:
    player = players[player]
    if player['clubId'] not in (None, ' ', False):
        clubId = player['clubId']
        if 'score' in player:
           if player['clubId'] in clubScore:
              clubMembers[clubId] += 1
              clubScore[clubId] += player['score']['score']
              clubIndividuals[clubId].append(player['score']['name'])
           else:
              clubMembers[clubId] = 0
              clubMembers[clubId] += 1
              clubScore[clubId] = 0
              clubScore[clubId] += player['score']['score']
              clubIndividuals[clubId] = [player['score']['name']]

sorted_clubScore = sorted(clubScore.items(), key=lambda x: x[1], reverse=True)

clubs = requests.get('https://exostats.nl/?api&clubs').json()
found = False
count = 1

print('pos:'.ljust(4),'club:'.ljust(20), 'score:'.ljust(11), 'mem:'.ljust(6), 'average:'.ljust(10),'players:')

for clubid, score in sorted_clubScore:
    for club in clubs['clubs']['data']:
        clubidnew = club[0]
        name = club[1]
        if clubidnew == clubid:
           found = True
           break
    if found == True:
       countDisplay = (str(count) + '.').ljust(4)
       name = name.ljust(20)
       totscore = str(round(score,3)).ljust(11)
       members = (str(clubMembers[clubid]) + '/15').ljust(6)
       avgscore = str(round(score/clubMembers[clubid],3)).ljust(10)
       memList = clubIndividuals[clubid]
       for cId in clubIndividuals:
           if cId == clubid:
              print(countDisplay,name,totscore,members,avgscore,memList)
              found = False
              count += 1

#clubs = requests.get('https://exostats.nl/?api&clubs').json()

#for i in range (len(clubs['clubs']['data'])):
#    if clubs['clubs']['data'][i][1] == 'im bad':
#        print(clubs['clubs']['data'][i])


#for i in range (20):
#    print(clubs['clubs']['data'][i])

#print(len(clubs['clubs']['data']))
#print(len(clubs['clubs']['data']))

#data = requests.get('https://exostats.nl/?rank=playerscore&asJson').json()

#player = data['G5kZMVeNecVtcQ98iNy3FgoWwv03']['score']


#print(len(data))
#print(player)

