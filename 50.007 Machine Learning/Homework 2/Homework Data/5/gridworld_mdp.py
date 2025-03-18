import matplotlib.pyplot as plt

import numpy as np


class simpleprob1():
  # all actions into one single state, the keystate, give a high reward

  def __init__(self,numh,numw, keystate):
  
    self.numh=numh
    self.numw=numw

    if (keystate[0]<0) or (keystate[0]>=self.numh):
      print('illegal')
      exit()
    if (keystate[1]<0) or (keystate[1]>=self.numw):
      print('illegal')
      exit()

    #state space: set of tuples (h,w) 0<=h<=numh, 0<=w<=numw
    self.statespace=[ (h,w) for h in range(self.numh) for w in range(self.numw) ]
 
    self.statespace2index=dict()
    for i,s in enumerate(self.statespace):
      self.statespace2index[s]=i



    self.actions=['stay','right','down','left','up']
    self.actdict2index=dict()
    for i,a in enumerate(self.actions):
      self.actdict2index[a]=i


    self.highrewardstate=keystate
    self.rewardtogothere=10.


  def transition_deterministic(self,oldstate_index,action):
    #P(s'|s,a) is 1 for one specific s'
    if action not in self.actions:
      print('illegal')
      exit()


    oldstate=self.statespace[oldstate_index]
    
    # all deterministic

    if self.actdict2index[action]==0:
      newstate=list(oldstate)

    elif self.actdict2index[action]==1:
      newstate=list(oldstate)
      newstate[1]=min(self.numw-1,newstate[1]+1)


    elif self.actdict2index[action]==2:
      newstate=list(oldstate)
      newstate[0]=min(self.numh-1,newstate[0]+1)


    elif self.actdict2index[action]==3:
      newstate=list(oldstate)
      newstate[1]=max(0,newstate[1]-1)


    elif self.actdict2index[action]==4:
      newstate=list(oldstate)
      newstate[0]=max(0,newstate[0]-1)

    #can return probs or set of new states and probabilities
    return self.statespace2index[tuple(newstate)]
  

  def reward(self,oldstate_index,action,newstate_index):
    #P(R|s,a)
    onlygoalcounts=True

    if False==onlygoalcounts: #one gets  a reward when one jumps into the golden state or stays there
      r=self.tmpreward1(oldstate_index, action, newstate_index)
    else: #one gets only a reward when one stays in the golden state
      r=self.tmpreward2(oldstate_index, action, newstate_index) 

    return r
  
  def tmpreward1(self,oldstate_index,action,newstate_index):

    newstate=self.statespace[newstate_index]
    if (newstate[0]==self.highrewardstate[0]) and (newstate[1]==self.highrewardstate[1]):
      return self.rewardtogothere
    else:
      return 0

  def tmpreward2(self,oldstate_index,action,newstate_index):

    newstate=self.statespace[newstate_index]
    if (newstate[0]==self.highrewardstate[0]) and (newstate[1]==self.highrewardstate[1]) and (action=='stay'):
      return self.rewardtogothere
    else:
      return 0




def plotqvalstable(qvals, simpleprob_instance, block):
  # input is numpy of shape (5,h,w)  
  #plotted into 3x3 + boundary  qvals[c,h,w] c=center,l,d,r,up
  plt.ion()

  offsets=[ [1,1],[1,2],[2,1],[1,0],[0,1] ]  
  symbols=[ 'o','->','\ ','<-','^' ]  

  mh=simpleprob_instance.numh
  mw=simpleprob_instance.numw

  plotvals=-np.ones((3*mh,3*mw))

  for i in range(len(simpleprob_instance.statespace)):
    h=simpleprob_instance.statespace[i][0]
    w=simpleprob_instance.statespace[i][1]

    for c in range( len(simpleprob_instance.actions)):
        plotvals[3*h + offsets[c][0] ,3*w+ offsets[c][1]]=qvals[ i,c]

  plotvals = np.ma.masked_where(plotvals<0,plotvals)

  fig, (ax0) = plt.subplots(1, 1)

  ax0.imshow(plotvals, cmap=plt.get_cmap('summer'),interpolation='nearest')



  #c = ax0.pcolor(plotvals, edgecolors='white', linewidths=1)
  ax0.patch.set(hatch='xx', edgecolor='red')

  for i in range(len(simpleprob_instance.statespace)):
    h=simpleprob_instance.statespace[i][0]
    w=simpleprob_instance.statespace[i][1]

    for c in range( len(simpleprob_instance.actions)):
      if c==0:
        printstr= "{:.2f}".format(qvals[ i,c]) #str(qvals[c,h,w])
      elif c==1:
        printstr="{:.2f}".format(qvals[ i,c])+symbols[c]
      elif c==2:
        printstr=symbols[c]+"{:.2f}".format(qvals[ i,c])
      elif c==3:
        printstr=symbols[c]+ "{:.2f}".format(qvals[ i,c])
      elif c==4:
        printstr=symbols[c]+"{:.2f}".format(qvals[ i,c])
      
              
      ax0.text( 3*w+ offsets[c][1], 3*h + offsets[c][0],printstr,
                     ha="center", va="center", color="k", fontsize=5)

  plt.draw()
  plt.pause(0.01)
  if True==block:
    input("Press [enter] to continue.")




def plotonlyvalstable2(qvals, simpleprob_instance,  block):
  # input is numpy of shape (5,h,w)  
  #plotted into 3x3 + boundary  qvals[c,h,w] c=center,l,d,r,up
  plt.ion()

  mh=simpleprob_instance.numh
  mw=simpleprob_instance.numw


  plotvals=-np.ones((mh,mw))
  for i in range(len(simpleprob_instance.statespace)):
    h=simpleprob_instance.statespace[i][0]
    w=simpleprob_instance.statespace[i][1]
    for c in range( len(simpleprob_instance.actions)):
      plotvals[h,w]=np.max(qvals[ i,:])

  plotvals = np.ma.masked_where(plotvals<0,plotvals)

  fig, (ax0) = plt.subplots(1, 1, figsize=(25, 25))  # Set the figsize to (10, 10) for a bigger figure

  ax0.imshow(plotvals, cmap=plt.get_cmap('summer'),interpolation='nearest')


  for h in range(mh):
    for w in range(mw):
      printstr= "{:.2f}".format(plotvals[h,w])   
      ax0.text( w, h ,printstr,ha="center", va="center", color="k")

  plt.draw()
  plt.pause(0.01)
  if True==block:
    #pass
    input("Press [enter] to continue.")





def q_value_iteration(problemclass, gamma, delta, showeveryiteration):
    numactions = len(problemclass.actions)  # Number of possible actions
    numstates = len(problemclass.statespace)  # Number of states in the grid world

    qsa = np.zeros((numstates, numactions))  # Initialize Q-values to zeros
    values = np.zeros(numstates)  # Initialize state values to zeros

    converged = False  # Convergence flag
    count = 0  # Iteration counter
    while not converged:
        nextqsa = np.zeros((numstates, numactions))  # Next iteration's Q-values
        nextvalues = np.zeros(numstates)  # Next iteration's state values
        diff = 0  # Maximum difference between current and next Q-values

        for s in range(numstates):  # Iterate over all states
            for a in range(numactions):  # Iterate over all actions
                newstate = problemclass.transition_deterministic(s, problemclass.actions[a])  # Determine the new state
                r = problemclass.reward(s, problemclass.actions[a], newstate)  # Calculate the reward
                nextqsa[s, a] = r + gamma * np.max(qsa[newstate])  # Update the Q-value using the Bellman equation
                diff = max(diff, abs(nextqsa[s, a] - qsa[s, a]))  # Update the maximum difference

            nextvalues[s] = np.max(nextqsa[s])  # Update the state value

        count += 1  # Increment the iteration counter
        if diff < delta:  # Check convergence based on the maximum difference
            converged = True

        qsa = nextqsa  # Update the Q-values with the next iteration's values
        values = nextvalues  # Update the state values with the next iteration's values

        if showeveryiteration:
            plotqvalstable(qsa, problemclass, block=True)  # Plot the Q-values at each iteration
    print("Final Q-values:")
    print(qsa)
    return values, qsa


 


def runmdp():

  plotbig=True
  showeveryiteration=True

  mdp=simpleprob1(5,6,keystate=[1,4])
  values,qsa=q_value_iteration(mdp,gamma=0.5, delta=3e-2, showeveryiteration=showeveryiteration)

  
  if False==plotbig:
    if False==showeveryiteration:
      plotonlyvalstable(qsa,  mdp, block=False)
  else:
    plotqvalstable(qsa, mdp,block=False)

  input("Press [enter] to continue.")

if __name__=='__main__':

  runmdp()