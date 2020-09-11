#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :shell.py
@Description  :a
@Datatime     :2020/08/29 20:15:09
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''


from km.drill.miner_supr import MinerSupr
from km.gen.gen_supervisor import GenSupervisor
from km.gen.gen_params import GenParams

import json
import sys


def runMiner(argv):
    action = argv[1]

    if action == 'generate':
        minerSupr = MinerSupr('')
        bgStr = minerSupr.bag()

        bgDict = json.loads(bgStr)
        address = bgDict['address']

        if len(argv) > 2:
            plotSize = int(argv[2])
        else:
            plotSize = 1
        genParams = GenParams(address, 1, plotSize, 1)

        genSupervisor = GenSupervisor(genParams)
        genSupervisor.init()
    elif action == 'mine':
        minerSupr = MinerSupr('')
        minerSupr.tick()

if __name__ == '__main__':
    runMiner(sys.argv)
