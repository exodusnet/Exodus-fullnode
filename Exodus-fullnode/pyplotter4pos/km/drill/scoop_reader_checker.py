#! /usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :scoop_reader_checker.py
@Description  :Type whatever you want
@Datatime     :2020/09/05 02:47:42
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

import os
from util.mining_plot import MiningPlot


class MsgReadScoops():
    def __init__(self, pi, scoopnum):
        self.filename = pi.filename
        self.address = pi.address
        self.startnonce = pi.startnonce
        self.plots = pi.plots
        self.staggeramt = pi.staggeramt
        self.scoopnum = scoopnum


class MsgScoopChunk():
    def __init__(self, address, startnonce, numscoops, scoops):
        self.address = address
        self.startnonce = startnonce
        self.numscoops = numscoops
        self.scoops = scoops


class ScoopReader():
    def __init__(self):
        pass

    def onReceive(self, message, sender):
        if type(message) == MsgReadScoops:
            self.__readFile(message, sender)

    def __readFile(self, msgReadScoops, sender):
        plotDir = self.__getPlotDir()
        with open(os.path.join(plotDir, msgReadScoops.filename), 'rb') as f:
            msgScoopChunks = list()
            chunksCount = int(msgReadScoops.plots / msgReadScoops.staggeramt)
            for i in range(0, chunksCount):
                f.seek((i * msgReadScoops.staggeramt * MiningPlot.PLOT_SIZE) + (msgReadScoops.scoopnum * msgReadScoops.staggeramt * MiningPlot.SCOOP_SIZE))
                len = msgReadScoops.staggeramt * MiningPlot.SCOOP_SIZE
                chunk = f.read(len)

                msgScoopChunk = MsgScoopChunk(msgReadScoops.address, msgReadScoops.startnonce + (i * msgReadScoops.staggeramt), msgReadScoops.staggeramt, chunk)
                sender.onReceive(msgScoopChunk)

    def __getPlotDir(self):
        import env
        root = env.getRoot()
        plotDir = os.path.abspath(os.path.join(root, 'plots'))

        return plotDir


class MsgCheckScoops():
    def __init__(self, msgScoopChunk, gensig):
        self.address = msgScoopChunk.address
        self.startnonce = msgScoopChunk.startnonce
        self.numscoops = msgScoopChunk.numscoops
        self.scoops = msgScoopChunk.scoops
        self.gensig = gensig


class MsgBestScoop():
    def __init__(self, address, nonce, result, proof):
        self.address = address
        self.nonce = nonce
        self.result = result
        self.proof = proof


class ScoopChecker():
    def __init__(self):
        pass

    def onReceive(self, message, sender):
        if type(message) == MsgCheckScoops:
            self.checkScoops(message, sender)

    def checkScoops(self, msgCheckScoops, sender):
        import util.ba as ba
        import util.shabal256 as shabal256

        lowest = int('FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF', 16)
        lowestscoop = 0
        lowestscoopProof = bytearray()

        mdBytes = bytearray(len(msgCheckScoops.gensig)+MiningPlot.SCOOP_SIZE)
        proof = bytearray(MiningPlot.SCOOP_SIZE)

        ba.copy(msgCheckScoops.gensig, 0, mdBytes, 0, len(msgCheckScoops.gensig))
        for i in range(0, msgCheckScoops.numscoops):
            ba.copy(msgCheckScoops.scoops, (i * MiningPlot.SCOOP_SIZE), mdBytes, len(msgCheckScoops.gensig), MiningPlot.SCOOP_SIZE)
            ba.copy(msgCheckScoops.scoops, (i * MiningPlot.SCOOP_SIZE), proof, 0, MiningPlot.SCOOP_SIZE)

            hex = shabal256.digestFromBytearray(mdBytes)
            hexBytes = bytes.fromhex(hex)
            ls = list(hexBytes)
            slicedList = ls[7::-1]
            anotherBytes = bytes(slicedList)

            num = int.from_bytes(anotherBytes, byteorder='big', signed=False)

            if num < lowest:
                lowest = num
                lowestscoop = msgCheckScoops.startnonce + i
                lowestscoopProof = proof

        sender.onReceive(MsgBestScoop(msgCheckScoops.address, lowestscoop, lowest,lowestscoopProof))