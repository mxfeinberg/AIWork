import re
import numpy as np
import random as rand
# This version of the code implements batch gradient descent and Cross validation
#


#Sigmoidal Activation Function
#if a prime = True is passed in, the function returns the derivatve

def threshold(val, prime = False):
    if(prime):
        #print sigmoid(val) * (1.0 - sigmoid(val))
        return np.multiply(sigmoid(val) , (1.0 - sigmoid(val)))
    else:
        return sigmoid(val)


def sigmoid(num):
    num = num * -1
    return 1/(1+np.exp(num))
    

def main():
    rand.seed()
    np.random.seed()
    h = 4
    a = 100 #number of attributes
    n = 7000 #numSamples
    w1BEST = np.random.random((h, a)) #should be 101 but weird data thing
    w2BEST = np.random.random((1, h))#NOTE: the dimensions of w1 and w2 were switched compared to the document
    b1BEST = np.zeros(shape=(h, 1))
    b2BEST = rand.random()
    lowestError = 100
    lastError = -1
    bestNodeNum = -1
    runNumber = -1
    path = "mushroom-training.txt"
    R = np.empty([1, n], dtype = np.int_) #Global Load Vector
    K = np.empty([a, n], dtype = np.int_) #Global stiffness matrix of testing data
    LEARNING_RATE = 0.2
    count = 0
    np.seterr(all='ignore')
    for line in open(path, 'r'):
        line = re.sub(',', '', line)
        R[0, count] = int(line[0])
        line = line[1:]
        length = len(line)
        for i in range(length-1):
            K[i, count] = int(line[0])
            line = line[1:]
        count+=1
    print('PARSING DONE')
    
    for j in range(0, 15):
        np.random.shuffle(K.T)
        trainingRatio = 6.0/7.0
        
        trainingSet = K[0:a,0:int(n*trainingRatio)]
        trainingSetR = R[0, 0:int(n*trainingRatio)]
        validationSet = K[0:a, int(n*trainingRatio):n]
        validationSetR = R[0,  int(n*trainingRatio):n]
        
        
        h = j+1#the number of hidden nodes
        #w1 = np.random.random((h, 100)) #should be 101 but weird data thing
        #w2 = np.random.random((1, h))#NOTE: the dimensions of w1 and w2 were switched compared to the document
        #b1 = np.zeros(shape=(h, 1))
        #b2 = rand.random()
        w1 = np.zeros((h, 100)) #should be 101 but weird data thing
        w2 = np.zeros((1, h))#NOTE: the dimensions of w1 and w2 were switched compared to the document
        b1 = np.zeros(shape=(h, 1))
        b2 = 0
        lastError = 100
        prevError = 100
        LEARNING_RATE = 0.01
        x = 0
        print('%i Node Run\n' % h)
        while(x < 100000):
            y0 = trainingSet
            x1 = np.dot(w1, y0)
            x1+=b1
            y1 = threshold(x1)
            x2 = np.dot(w2,y1)
            x2+=b2
            y2 = threshold(x2)
            
            delta2 = np.multiply(threshold(x2, prime = True), (y2-trainingSetR))
            
            
            bchange = np.empty([h,], dtype = np.float_)
            delta1 = np.sum(np.multiply(threshold(x1, prime = True), (w2.T * delta2)), axis = 1, out = bchange, keepdims = False)
            delta1  = delta1.reshape((h, 1))
         
            deltaW2 = np.dot(np.multiply(threshold(x2, prime = True), (y2 - trainingSetR)), y1.T)
            
            deltaW1 = np.dot(np.multiply(threshold(x1, prime = True), (w2.T * delta2)), y0.T)
        
            w2-=(LEARNING_RATE*deltaW2)
            w1-=(LEARNING_RATE*deltaW1)
            b1-=(LEARNING_RATE*delta1)
            delta2 = np.sum(delta2)
            b2-=(LEARNING_RATE*delta2)

            y0V = validationSet
            x1V = np.dot(w1, y0V)
            x1V +=b1
            y1V = threshold(x1V)
            x2V = np.dot(w2,y1V)+b2
            y2V = threshold(x2V)
            
            
            MSE = (0.5*(np.square(validationSetR-y2V)).mean(axis = None)) #calculate error yo
            prevError = MSE
            if(MSE < lowestError):
                w1BEST = w1
                w2BEST = w2
                b1BEST = b1
                b2BEST = b2
                bestNodeNum = h
                lowestError = MSE
                runNumber = x
            
            if(x % 10 == 0):
                if(abs(MSE - lastError) < 0.001 and x % 500 == 0):
                    break
                lastError = MSE
                print("Error: " + str(MSE) + " Run: " + str(x) + " Number of Hidden Nodes: " + str(h) + "\nLowest Error: " + str(lowestError) )
                    
                    
                
            LEARNING_RATE = LEARNING_RATE - (0.001/100000)
            x+=1
                
    np.save('weights1_1.npy', w1BEST)
    np.save('weights2_1.npy', w2BEST)
    np.savetxt('weights1_1.txt', w1BEST)
    np.savetxt('weights2_1.txt', w2BEST)
    np.save('base1_1.npy', b1BEST)
    np.save('base2_1.npy', b2BEST)
    np.savetxt('base1_1.txt', b1BEST)
    np.savetxt('base2_1.txt', b2BEST)
    
    print(bestNodeNum)
    print(lowestError)
    print(runNumber)
    
    
    text_file = open("NodeNumber.txt", "w")
    text_file.write("Number of Nodes: " + str(bestNodeNum) + "\nError: " + str(lowestError) +"\nNumber of Runs: " + str(runNumber) )
    text_file.close()
    
main()