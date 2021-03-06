from flask import jsonify, request
from . import api
from .. import db
from ..models import Travel, TravelCard
from ..errors import bad_request
from functools import reduce


@api.route('/sync/count')
def get_count():
    travels = Travel.query.filter_by(userId=request.user.id)
    travels = [travel.to_json() for travel in travels]
    travelCount = Travel.query.filter_by(userId=request.user.id, isDelete=False).count()
    travelCardCount = 0

    for travel in travels:
        travelCardCount += TravelCard.query.filter_by(id=travel['serverId'], isDelete=False).count()

    return jsonify({
        'travelCount': travelCount,
        'travelCardCount': travelCardCount
    }), 200


@api.route('/sync/travels', methods=['POST'])
def create_travels():
    travels = request.json['travels']
    tempTravels = []
    for item in travels:
        item = Travel(userId=request.user.id,
                        localId=item.get('localId'),
                        startDate=item.get('startDate'),
                        endDate=item.get('endDate'),
                        travelKind=item.get('travelKind'),
                        area=item.get('area'),
                        title=item.get('title'),
                        theme=item.get('theme'),
                        imageName=item.get('imageName'),
                        imageUrl=item.get('imageUrl'))
        db.session.add(item)
        tempTravels.append(item)
    try:
        db.session.commit()
    except:
        db.session.rollback()
    createdTravels = [travel.sync_create_json() for travel in tempTravels]
    return jsonify({ 'travels': createdTravels })


@api.route('/sync/travels', methods=['PUT'])
def update_travels():
    reqTravels = request.json['travels']
    for reqTravel in reqTravels:
        travel = Travel.query.get(reqTravel['serverId'])
        if reqTravel['title'] is not None:
            travel.title = reqTravel['title']
        if reqTravel['area'] is not None:
            travel.area = reqTravel['area']
        if reqTravel['startDate'] is not None:
            travel.startDate = reqTravel['startDate']
        travel.endDate = reqTravel['endDate']
        if reqTravel['travelKind'] is not None:
            travel.travelKind = reqTravel['travelKind']
        if reqTravel['theme'] is not None:
            travel.theme = reqTravel['theme']
        if reqTravel['imageName'] is not None:
            travel.imageName = reqTravel['imageName']
        if reqTravel['imageUrl'] is not None:
            travel.imageUrl = reqTravel['imageUrl']
        if reqTravel['share'] is not None:
            travel.share = reqTravel['share']

    try:
        db.session.commit()
        return jsonify({ 'result': 'Update is successed!' }), 200
    except:
        return jsonify({ 'result': 'Update is failed.' }), 500


@api.route('/sync/travelcards', methods=['POST'])
def create_travelcards():
    travelCards = request.json['travelCards']
    tempTravelCards = []
    for item in travelCards:
        if item.get('localId') is None:
            return bad_request(400, 'localId is required.')
        if TravelCard.query.filter_by(localId=item.get('localId')).first():
            return bad_request(400, 'LocalId already exist.')
        if item.get('travelServerId') is None:
            return bad_request(400, 'travelSercerId is required.')
        if item.get('travelLocalId') is None:
            return bad_request(400, 'travelLocalId is required.')

        newItem = TravelCard(travelId=item.get('travelServerId'),
                                localId=item.get('localId'),
                                travelLocalId=item.get('travelLocalId'),
                                travelOfDay=item.get('travelOfDay'),
                                country=item.get('country'),
                                theme=item.get('theme'),
                                content=item.get('content'),
                                imageName=item.get('imageNames'),
                                imageUrl=item.get('imageUrl'),
                                date=item.get('date'))
        db.session.add(newItem)
        tempTravelCards.append(newItem)

    try:
        db.session.commit()
        createdTravelCards = [travelCard.sync_create_json() for travelCard in tempTravelCards]
        return jsonify({ 'travelCards': createdTravelCards })
    except:
        db.session.rollback()
        return jsonify({ 'result': 'Create travel cards sync is failed.'}), 500


@api.route('/sync/travelcards', methods=['PUT'])
def update_travelcards():
    reqTravelCards = request.json['travelCards']
    for reqTravelCard in reqTravelCards:
        if reqTravelCard['serverId'] is None:
            return bad_request(400, 'serverId is required for update.')
        travelCard = TravelCard.query.get(reqTravelCard['serverId'])
        if reqTravelCard.get('content') is not None:
            travelCard.content = reqTravelCard.get('content')
        if request.json.get('country') is not None:
            travelCard.country = request.json.get('country')
        if request.json.get('theme') is not None:
            travelCard.theme = request.json.get('theme')
        if request.json.get('imageNames') is not None:
            travelCard.imageName = request.json.get('imageNames')
        if request.json.get('imageUrl') is not None:
            travelCard.imageUrl = request.json.get('imageUrl')
        if request.json.get('date') is not None:
            travelCard.date = request.json.get('date')
        if request.json.get('time') is not None:
            travelCard.time = request.json.get('time')
    
    try:
        db.session.commit()
        return jsonify({ 'result': 'Update is successed!' }), 200
    except:
        return jsonify({ 'result': 'Update is failed.' }), 500
