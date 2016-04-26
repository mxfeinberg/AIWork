# -*- coding: utf-8 -*-
"""
Created on Thu Apr 21 18:46:17 2016

@author: Max
"""

from distutils.core import setup
from Cython.Build import cythonize

setup(
    ext_modules = cythonize("neuralNetsWithCross.pyx")
)
