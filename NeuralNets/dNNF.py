import numpy as np
import time
import re
import random as rand

"""
Threshold function for the neural net
"""
def threshold(val, prime = False):
    if(prime):
        return sigmoid(val) * (1.0 - sigmoid(val))
    else:
        return sigmoid(val)
"""
Sigmoid helper function for the threshold function
"""

def sigmoid(num):
    num = num * -1
    return 1/(1+np.exp(num))
    
lastError = 100


runNum = 0
lineNum = 0

R = np.empty([1, 7000], dtype = np.int_) #Global Load Vector
K = np.empty([100, 7000], dtype = np.int_) #Global stiffness matrix of testing data
count = 0
#Parse in the data into two matrices
path = 'mushroom-training.txt'
np.seterr(all='ignore')
for line in open(path, 'r'):
    line = re.sub(',', '', line)
    line = re.sub('\n', '', line)
    line = map(int, line)
    R[0, count] = line.pop(0)
    length = len(line)
    for i in range(length):
        K[i, count] = line.pop(0)
    count+=1

print('PARSING DONE')

learningRate = 0.20


h = 4

wN = np.empty(50000)
w1 = np.random.uniform(0,1,size =(h, 100))
delta1 = np.zeros(shape=(h,100))
b1 = np.random.uniform(0,1, size=(h, 1))



w2 = np.random.uniform(0, 1, size = (1, h))
delta2 = np.zeros(shape=(1,h))
b2 = rand.random()


while runNum < 50000:
	
	
	y0 = K[0:100, runNum % 6000].reshape(100,1)
	
	
	x1 = np.dot(w1, y0) + b1

	y1 = threshold(x1)

	x2 = np.dot(w2, y1) + b2
	y2 = threshold(x2)

	
	rInstance = R[0, runNum % 6000].reshape(1)
	
	for i in range(len(w2)): #Should be number of out nodes
		for j in range(len(w2[i])): #Should be number of input nodes
			delta2[i][j] = threshold(y2[i], prime = True) * (y2[i] - rInstance)
			dW = delta2[i][j] * y1[j]
			#b2 = b2 - (learningRate*delta2[i][j])
			w2[i][j] = w2[i][j] - (learningRate * dW)
	

	for i in range(len(w1)):
		for j in range(len(w1[i])):
			#b1[i][0] = b1[i][0] -(learningRate * threshold(x1[i][0], prime = True) * delta2[0][i] * w2[0][i])
			dW = delta2[0][i] * y0[j] * w2[0][i] * threshold(y1[i], prime = True)
			w1[i][j] = w1[i][j] - (learningRate * dW)
			
	y0V = K
	x1V = np.dot(w1, y0V)
	y1V = threshold(x1V)
	x2V = np.dot(w2, y1V)
	y2V = threshold(x2V)
	MSE = (0.5*(np.square(R-y2V)).mean(axis = None))
	wN[runNum] = MSE
	if runNum % 1000 == 0:
		#y0V = K
		#x1V = np.dot(w1, y0V)
		#y1V = threshold(x1V)
		#x2V = np.dot(w2, y1V)
		#y2V = threshold(x2V)
		#MSE = (0.5*(np.square(R-y2V)).mean(axis = None)) #calculate error yo
		print "RUN NUMBER: "+ str(runNum) 
		print "TARGET VAL VS OUTPUT: " + str(R[0][0]) + ", " + str(y2V[0][0])
		print "ERROR: " + str(MSE)
		print w2
		print ""
		#if(MSE < 0.05):
		#	print "LAST RUN"
		#	print "Run Number: " + str(runNum)
		#	break
		#if(abs(MSE-lastError) < 0.00001):
		#	print "LAST RUN"
		#	print "Run Number: " + str(runNum)
		#	break
		learningRate = learningRate - (0.1/500000)
		lastError = MSE
	runNum = runNum + 1
np.savetxt('Error.txt', wN)
print "%%%%%%%%%%%%%%%%%%%%%%"
print "Testing will now begin"
print "%%%%%%%%%%%%%%%%%%%%%%"


f = open('mushroom-testing.txt', 'r').readlines()
runNum = 0
lineNum = 0

testR = np.empty([1, 1000], dtype = np.int_) #Global Load Vector
testK = np.empty([100, 1000], dtype = np.int_) #Global stiffness matrix of testing data
count = 0
path = 'mushroom-testing.txt'
np.seterr(all='ignore')
for line in open(path, 'r'):
    line = re.sub(',', '', line)
    line = re.sub('\n', '', line)
    line = map(int, line)
    testR[0, count] = line.pop(0)
    length = len(line)
    for i in range(length):
        testK[i, count] = line.pop(0)
    count+=1
    
y0V = testK
x1V = np.dot(w1, y0V)
y1V = threshold(x1V)
x2V = np.dot(w2, y1V)
y2V = threshold(x2V)
diff = np.around(testR-y2V)
MSE = ((diff).mean(axis = None)) #calculate error yo
print('Testing Error: ' + str(MSE))