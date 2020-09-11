#! /usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :miner_supr.py
@Description  :Type whatever you want
@Datatime     :2020/09/04 23:13:33
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

from km.drill.miner import MsgBestResult
from km.drill.miner import Miner
from km.drill.miner_com import MinerCom
from km.drill.miner_com import MsgRefreshNetState
from km.drill.miner_com import MsgGetBag
from km.drill.miner_supr_msg import NetState
from km.drill.miner_supr_msg import MsgGetBagResult
from util.h import Http

from env import reward_url

import os


class MinerSupr():
    def __init__(self, address):
        self.miner = None
        self.com = None

        self.address = address
        self.state = None
        self.__init()

    def __init(self):
        # load passphrase and build com
        self.com = MinerCom(self)

    def onReceive(self, message):
        if type(message) == NetState:
            self.state = message

            self.miner = Miner(self.state, self)
        elif type(message) == MsgBestResult:
            from util.convert import Convert

            url = reward_url

            print('New best: %s,Best nonce: %s,Proof: %s' % (Convert.parseUnsignedLong(message.bestaddress), message.bestnonce,message.proof))

            data = {}

            gensig = '1C532145697A8B6F'
            string = 'abcdefgh'
            bys = bytes(string, encoding="utf8")
            proof = str(bys, encoding="utf8")
            bag = self.bag()

            data['gensig'] = gensig
            data['proof'] = message.proof
            data['bag'] = bag


            newUrl = Http.newUrl(url,{'requestType':'participateinTaiSai'})
            resp = Http.post(newUrl,data)
            print('resp: %s' % resp)

        elif type(message) == MsgGetBagResult:
            passphrasesDir = self.__getPassphrasesDir()
            passFile = os.path.join(passphrasesDir,'.passphrases')
            cleanBag = self.__cleanUnexpectedCh(message.bag)

            with open(passFile, 'w') as  pf:
                pf.write(cleanBag)


    def tick(self):
        self.bag()
        self.com.onReceive(MsgRefreshNetState())

    def bag(self):
        bg = ''
        passphrasesDir = self.__getPassphrasesDir()
        passFile = os.path.join(passphrasesDir, '.passphrases')

        if not os.path.isfile(passFile):
            self.com.onReceive(MsgGetBag())

        with open(passFile, 'r') as  pf:
            bg = pf.read()

        return bg

    def __getPassphrasesDir(self):
        import env
        root = env.getRoot()
        plotDir = os.path.abspath(os.path.join(root, 'plots'))

        if not os.path.isdir(plotDir):
            os.mkdir(plotDir)

        return plotDir

    def __cleanUnexpectedCh(self, str):
        cleanedStr = str
        if str[0] == '"' and str[-1] == '"':
            cleanedStr = str[1:-1]

        cleanedStr = eval(repr(cleanedStr).replace('\\', ''))
        return cleanedStr


