#! /usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :km.py
@Description  :Type whatever you want
@Datatime     :2020/09/04 19:55:48
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

from util.mining_plot import MiningPlot

from km.drill.scoop_reader_checker import ScoopChecker
from km.drill.scoop_reader_checker import ScoopReader
from km.drill.scoop_reader_checker import MsgReadScoops
from km.drill.scoop_reader_checker import MsgScoopChunk
from km.drill.scoop_reader_checker import MsgBestScoop
from km.drill.scoop_reader_checker import MsgCheckScoops

from km.drill.miner_msg import PlotInfo
from km.drill.miner_msg import MsgSendResults
from km.drill.miner_msg import MsgBestResult

import util.ba as ba

import os
from threading import Timer


class Miner():
    def __init__(self, netState, minerSupr):

        self.state = netState
        self.reader = ScoopReader()
        self.checker = ScoopChecker()

        self.parent = minerSupr
        self.bag = minerSupr.bag()

        self.__init()

    def __init(self):
        import util.shabal256 as shabal256

        self.bestresult = int('FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF', 16)
        self.bestaddr = 0
        self.bestnonce = 0
        self.newbest = False

        heightBytes = self.state.height.to_bytes(length=8, byteorder='big', signed=False)

        buf = bytearray(len(self.state.gensig) + 8)
        ba.copy(self.state.gensig, 0, buf, 0, len(self.state.gensig))
        ba.copy(heightBytes, 0, buf, len(self.state.gensig), 8)

        hex = shabal256.digestFromBytearray(buf)
        hexBytes = bytes.fromhex(hex)
        hashnum = int.from_bytes(hexBytes, byteorder='big', signed=False)
        self.scoopnum = hashnum % MiningPlot.SCOOPS_PER_PLOT

        plotFiles = self.__getAllPlotFiles()
        for i in range(0, len(plotFiles)):
            pi = PlotInfo(plotFiles[i])

            self.reader.onReceive(MsgReadScoops(pi, self.scoopnum), self)

        self.timer = Timer(5, self.onReceive, (MsgSendResults(),))
        self.timer.start()

    def onReceive(self, message):
        if type(message) == MsgScoopChunk:
            self.checker.onReceive(MsgCheckScoops(message, self.state.gensig), self)
        elif type(message) == MsgBestScoop:
            if message.result < self.bestresult:
                self.bestresult = message.result
                self.bestaddr = message.address
                self.bestnonce = message.nonce
                self.newbest = True
                self.proof = message.proof
        elif type(message) == MsgSendResults:
            if self.newbest:
                self.parent.onReceive(MsgBestResult(self.bestaddr, self.bestnonce, self.proof))

    def __getAllPlotFiles(self):
        import env

        plotFiles = []
        folder = os.path.join(env.getRoot(), 'plots')
        for filename in os.listdir(folder):
            fullFileName = os.path.join(folder, filename)
            if os.path.isfile(fullFileName) and filename != '.passphrases':
                plotFiles.append(filename)

        return plotFiles

    def __del__(self):
        if self.timer is not None:
            self.timer.cancel()