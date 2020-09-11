#!/usr/bin/env python
# -*- encoding: utf-8 -*-
'''
@File         :Shabal256.py
@Description  :ensuring that py-sph-shabal is available
@Datatime     :2020/08/31 03:26:56
@Author       :Francis(francis_xiiiv@163.com)
@version      :1.0
'''


# from py_sph_shabal import shabal256


def digest(str):
    # return shabal256(str).hex()
    import ctypes
    import binascii
    import env

    libshabalSoName = './libshabal.' + env.os + '.so'

    charPointer = ctypes.c_char_p()
    charPointer.value = str.encode()
    r = ctypes.create_string_buffer(32)

    libshabal = ctypes.cdll.LoadLibrary('./libshabal.linux.so')
    libshabal.shabal256(charPointer, r)
    rVal = r.value

    return binascii.hexlify(rVal).decode()


# def digestIntoBytearray(str):
#     '''
#     'd55e745e7bfb395e6cc13a5c57b2972100ce2fb18a25041ab2fea333c2e9e425'  => bytearray(b'\xd5^t^{\xfb9^l\xc1:\\W\xb2\x97!\x00\xce/\xb1\x8a%\x04\x1a\xb2\xfe\xa33\xc2\xe9\xe4%')
#     '''
#     return bytearray.fromhex(shabal256(str).hex())


def digestFromBytearray(bytearrayObj):
    '''
    b'\\xf0\\xf1\\xf2' => 'f0f1f2'  => fd9b8a9e996cd32b6ea7200db71011f31cb3a6be9e57f8f1b63009f6f00483e7
    '''
    hexadecimalStr = bytearray(bytearrayObj).hex()
    return digest(str(hexadecimalStr))
