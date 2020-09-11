# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :gen_supervisor.py
@Description  :Type whatever you want
@Datatime     :2020/09/03 19:05:49
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

from km.gen.plot_generator import PlotGenerator
from km.gen.plot_generator import MsgGenerate
from util.mining_plot import MiningPlot
import util.ba as ba

import os


class GenSupervisor():
    def __init__(self, condition):
        self.__currentNonce = 0
        self.__recvresults = 0
        self.__outbuffer = None
        self.__generatedBytesAmount = 0
        self.__out = None

        self.condition = condition
        self.pg = PlotGenerator()

    def init(self):
        self.__currentNonce = self.condition.startnonce
        self.__outbuffer = bytearray(self.condition.plots * MiningPlot.PLOT_SIZE)

        outname = '{}_{}_{}_{}'.format(self.condition.addr, self.condition.startnonce, self.condition.plots, self.condition.staggeramt)

        plotDir = self.__getPlotDir()
        if not os.path.exists(plotDir):
            os.makedirs(plotDir)
        # self.__out = open('./plots/' + outname, 'wb')
        self.__out = open(os.path.join(plotDir, outname), 'wb')

        self.__sendWork()

    def __sendWork(self):
        self.__recvresults = 0
        print('Generating from nonce: %d' % self.__currentNonce)

        for i in range(0, self.condition.staggeramt):
            msgGenerateResult = self.pg.receive(MsgGenerate(self.condition.addr, self.__currentNonce + i))
            self.__receive(msgGenerateResult)

    def __receive(self, msgGenerateResult):
        self.__recvresults = self.__recvresults + 1
        self.__processPlot(msgGenerateResult.plot, msgGenerateResult.nonce)
        staggeramt = self.condition.staggeramt
        startnonce = self.condition.startnonce
        plots = self.condition.plots

        if self.__recvresults >= staggeramt:
            print("Writing from nonce %d" % self.__currentNonce)
            len = self.__out.write(self.__outbuffer)
            self.__generatedBytesAmount = self.__generatedBytesAmount + len
            self.__currentNonce = self.__currentNonce + staggeramt

            if (self.__currentNonce < startnonce + plots):
                self.__sendWork()
            else:
                self.__out.close()

    def __processPlot(self, p, nonce):
        off = nonce - self.__currentNonce
        staggeramt = self.condition.staggeramt

        for i in range(0, MiningPlot.SCOOPS_PER_PLOT):
            ba.copy(p.data, i * MiningPlot.SCOOP_SIZE, self.__outbuffer, (i * MiningPlot.SCOOP_SIZE * staggeramt) + (off * MiningPlot.SCOOP_SIZE), MiningPlot.SCOOP_SIZE)

    def __getPlotDir(self):
        import env
        root = env.getRoot()
        plotDir = os.path.abspath(os.path.join(root, 'plots'))

        return plotDir
