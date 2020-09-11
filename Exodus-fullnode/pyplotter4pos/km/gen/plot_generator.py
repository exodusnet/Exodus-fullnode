
# !/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :plot_generator.py
@Description  :Type whatever you want
@Datatime     :2020/09/03 18:50:28
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''

from util.mining_plot import MiningPlot


class MsgGenerate():
    def __init__(self, id, nonce):
        self.id = id
        self.nonce = nonce


class MsgGenerateResult():
    def __init__(self, nonce, plot):
        self.plot = plot
        self.nonce = nonce


class PlotGenerator():
    def receive(self, msgGenerate):
        plotter = MiningPlot(msgGenerate.id, msgGenerate.nonce)
        return MsgGenerateResult(msgGenerate.nonce, plotter)
