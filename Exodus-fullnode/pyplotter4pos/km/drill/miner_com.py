#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :miner_com.py
@Description  :Type whatever you want
@Datatime     :2020/09/05 01:35:40
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

from km.drill.miner_supr_msg import MsgSubmitResult
from km.drill.miner_supr_msg import NetState
from km.drill.miner_supr_msg import MsgGetBagResult
from km.drill.miner_com_msg import MsgRefreshNetState
from km.drill.miner_com_msg import MsgGetBag
from util.convert import Convert
from util.h import Http
import json

from env import reward_url


class MinerCom():
    def __init__(self, minerSupr):
        self.parent = minerSupr

    def onReceive(self, message):
        if type(message) == MsgRefreshNetState:
            h = '11'
            b = bytearray.fromhex('1C532145697A8B6F')
            height = Convert.parseUnsignedLong(h)
            state = NetState(height, b)
            self.parent.onReceive(state)

        elif type(message) == MsgSubmitResult:
            pass
        elif type(message) == MsgGetBag:
            url = reward_url
            resp = Http.get(url, {'requestType': 'makeBag'})
            bag = json.loads(resp)
            b = bag.get('bag')
            self.parent.onReceive(MsgGetBagResult(json.dumps(b)))

