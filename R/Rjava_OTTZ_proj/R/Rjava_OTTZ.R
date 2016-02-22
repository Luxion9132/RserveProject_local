
#param:TrainURL, e.g. "http://ichart.finance.yahoo.com/table.csv?s=2107.TW&a=00&b=01&c=2013&d=11&e=31&f=2013&g=d" #a and d is month start from 00
#return: Bestn, Bestk
#errormsg, if throw exception
filterTraining<- function(TrainData){  
  
  #caculat
  tryCatch({
    CompareTable <- data.frame(j = NA, n = NA, kp = NA, AveReturn = NA) #declare a Table for performance comparison
    i <- 0.01 #K% start from 0.001 and increasing by 0.001
    j <- 1 # to make how many loop it have been done
    n <- 2 # MA(n) = 5
    Max.n <- dim(TrainData)[1]
    Bestn <- 0
    Bestk <- 0
    
    for (n in seq(2, Max.n, by=3)){
      kp <- 0
      repeat{
        kp <- kp + i
        filter1 <- try(OzFilter(TrainData, nday = n, k = kp),silent = F) #building the filterRules model and caculate the averege Revenue
        r <- rbind(filter1$r)[, 2] #get the revenue list
        AvgR <- mean(r) #caculate the averege Revenue
        if (is.na(AvgR)) {break()}
        CompareTable[j, ] <- c(j, n, kp, AvgR) #insert to CompareTable
        j = j + 1
        if (kp > 0.5) {break()} #stop if K% over 50%
      }
      
    }
    max <- which.max(CompareTable[, 4]) #search which K% is best
    CompareTable[max, ] #select the best K% from CompareTable
    Bestn <- CompareTable[max, 2] #pick up best n
    Bestk <- CompareTable[max, 3] #pick up bes k
    if (is.logical(Bestn)) # if no Best n and Best K
    {Bestn <- 0
     Bestk <- 0
    }
  }, error = function(e) {
    errormsg<<-conditionMessage(e)
  })
  result<- list(Bestn = Bestn, Bestk = Bestk)
}

#param:TestURL, Bestn, Bestk, plotUrl
#return: revenue, buysell, final; and it build plotpic in direction like 'D:/Capture'
#errormsg, if throw exception
filterTesting<- function(Testdata, Bestn, Bestk, plotUrl){  
    
    final <- OzFilter(Testdata, nday = Bestn, k = Bestk)
    buysell<-final$buysell
    
    #plot testData
    #png('D:/rplot.png',width = 1200, height = 600, units = "px")
    png(plotUrl,width = 1200, height = 600, units = "px")
    par(mfrow = c(1,1)) #re-set 1*1 plot
    plotFilter_v2(Testdata, final, Bestn)
    dev.off() 
  result <- list(revenue = mean(final$r[, 2]), buysell = buysell, final= final, Testdata = Testdata)
}

plotFilter_v2 <- function(x, object, Bestn)
{
  x = x
  buysell<-as.data.frame(rbind(object$buysell))
  
  if (colnames(x)[1] == 'Date') #determine date or mintue data
  {
    plot(x[ ,1], x[ ,5], type='l', xlab='Date', ylab='Close Price', las=2 ,main='' )
    lines(object$mydatadat,SMA(x[ ,5], n = Bestn), col='orangered2')
    points(as.POSIXlt(buysell$buydate),as.numeric(buysell$buyprice),type = "p",col='red',pch=16)
    points(as.POSIXlt(buysell$selldate),as.numeric(buysell$sellprice),type = "p",col='blue3',pch=17)
    text(as.POSIXlt(buysell$buydate),as.numeric(buysell$buyprice), adj = c(0.5,-1),labels='BUY',col='red')
    text(as.POSIXlt(buysell$buydate),as.numeric(buysell$buyprice), adj = c(0.5,1.5),labels=as.numeric(as.character(buysell$buyprice)),col='red',font=2)
    text(as.POSIXlt(buysell$buydate,),as.numeric(buysell$buyprice), adj = c(0.5,3),labels=buysell$buydate,col='red')
    text(as.POSIXlt(buysell$selldate),as.numeric(buysell$sellprice), adj = c(0.5,-1),labels='SELL',col='blue3')
    text(as.POSIXlt(buysell$selldate),as.numeric(buysell$sellprice), adj = c(0.5,1.5),labels=as.numeric(as.character(buysell$sellprice)),col='blue3',font=2)
    text(as.POSIXlt(buysell$selldate),as.numeric(buysell$sellprice), adj = c(0.5,3),labels=buysell$selldate,col='blue3')
  }else if (colnames(x)[1] == 'DateTime')
  {
    plot(x[ ,5], type='l', xlab='DateTime', ylab='Close Price', las=2, xaxt='n' ,main='' )
    atn <- seq(1, dim(x)[1], by=30) #set up axis gap
    x[atn, 1]
    axis(1,at=atn, labels=x[atn, 1],las=1,cex.axis=0.7)
    buypoint <- match(as.POSIXlt(buysell$buydate),x$DateTime) # match is used to find the rownumber
    sellpoint <- match(as.POSIXlt(buysell$selldate),x$DateTime) 
    mydatepoint <- match(as.POSIXlt(object$mydatadate),x$DateTime)
    lines(mydatepoint,SMA(x[ ,5], n = Bestn), col='orangered2')
    points(buypoint,as.numeric(buysell$buyprice),type = "p",col='red',pch=16)
    points(sellpoint,as.numeric(buysell$sellprice),type = "p",col='blue3',pch=17)
    text(buypoint,as.numeric(buysell$buyprice), adj = c(0.5,-1),labels='BUY',col='red')
    text(buypoint,as.numeric(buysell$buyprice), adj = c(0.5,1.5),labels=as.numeric(as.character(buysell$buyprice)),col='red',font=2)
    text(buypoint,as.numeric(buysell$buyprice), adj = c(0.5,3),labels=buysell$buydate,col='red')
    text(sellpoint,as.numeric(buysell$sellprice), adj = c(0.5,-1),labels='SELL',col='blue3')
    text(sellpoint,as.numeric(buysell$sellprice), adj = c(0.5,1.5),labels=as.numeric(as.character(buysell$sellprice)),col='blue3',font=2)
    text(sellpoint,as.numeric(buysell$sellprice), adj = c(0.5,3),labels=buysell$selldate,col='blue3')
  }else
    plot(x[ ,5], type='l')
}

maTraining <- function(TrainData){
  if (exists('setuploop')){
  }else {
    setuploop <- function(x,...)
    {
      x=x
      Max.l <- dim(x)[1]
      if ( Max.l > 130)
      {
        Max.l <- 130
      } else {
        Max.l <- dim(x)[1]
      }
      
      Max.l  
    }
  }
  errormsg<<-0
  
  #caculate
  tryCatch({
    CompareTable <- data.frame(j = NA, n = NA, l = NA, AveReturn = NA) #declare a Table for performance comparison
    j <- 1 # to record loop number
    Max.l <- setuploop(TrainData)
    Max.n <- 90
    Bests <- 0
    Bestl <- 0
    
    for (n in seq(3, Max.n, by = 2)){  # Sday start from 3, lag 2
      for (l in seq(5, Max.l, by = 3)){ # Lday start from 5, lag 3
        repeat{
          if (n > l) {break()}
          ma1 <- try(OzMa(TrainData, Sday = n, Lday = l),silent = F) #building the filterRules model and caculate the averege Revenue
          r <- rbind(ma1$r)[, 2] #get the revenue list
          AvgR <- mean(r) #caculate the averege Revenue
          if (is.na(AvgR)) {break()}
          CompareTable[j, ] <- c(j, n, l, AvgR) #insert to CompareTable
          j = j + 1
          break()
        }
      }
    }
    max <- which.max(CompareTable[, 4]) #search which K% is best
    CompareTable[max, ] #select the best K% from CompareTable
    Bests <- CompareTable[max, 2] #pick up best n
    Bestl <- CompareTable[max, 3] #pick up best l 
  }, error = function(e) {
    errormsg<<-conditionMessage(e)
  })
  result<- list(Bests = Bests, Bestl = Bestl)
}

maTesting <- function(Testdata, Bests, Bestl, plotUrl){  
  
  tryCatch({
    final <- OzMa(Testdata, Sday = Bests, Lday = Bestl)
    buysell<-as.data.frame(rbind(final$buysell))
  }, error = function(e) {
    errormsg<<-conditionMessage(e)
  })

  png(plotUrl,width = 1200, height = 600, units = "px")
  plotOzMa(Testdata,final)
  dev.off()
  result <- list(revenue = mean(final$r[, 2]), buysell = buysell, final= final, Testdata = Testdata)
}

rsiTraining <- function(TrainData){
  if (exists('setuploop')){
  }else {
    setuploop <- function(x,...)
    {
      x=x
      Max.l <- dim(x)[1]
      if ( Max.l > 133)
      {
        Max.l <- 130
      } else {
        Max.l <- dim(x)[1]-4
      }
      
      Max.l  
    }
  }
  
  errormsg<<-0
  #caculate
  tryCatch({
    CompareTable <- data.frame(j = NA, n = NA, l = NA, AveReturn = NA) #declare a Table for performance comparison
    j <- 1 # to record loop number
    Max.l <- setuploop(TrainData) #RSI need to monitor in next 3 days.
    Max.n <- 90
    Bests <- 0
    Bestl <- 0
    
    for (n in seq(3, Max.n, by = 2)){ # Sday start from 3, lag 2
      for (l in seq(12, Max.l, by = 3)){ # Lday start from 12, lag 3
        repeat{
          if (n > l) {break()}
          Rsi1 <- try(OzRsi(TrainData, Sday = n, Lday = l),silent = F) #building the model and caculate the averege Revenue
          r <- rbind(Rsi1$r)[, 2] #get the revenue list
          AvgR <- mean(r) #caculate the averege Revenue
          if (is.na(AvgR)) {break()}
          CompareTable[j, ] <- c(j, n, l, AvgR) #insert to CompareTable
          j = j + 1
          break()
        }
      }
    }
    max <- which.max(CompareTable[, 4]) #search which K% is best
    CompareTable[max, ] #select the best K% from CompareTable
    Bests <- CompareTable[max, 2] #pick up best n
    Bestl <- CompareTable[max, 3] #pick up best l 
  }, error = function(e) {
    errormsg<<-conditionMessage(e)
  })
  result<- list(Bests = Bests, Bestl = Bestl)
}

rsiTesting <- function(TestData, Bests, Bestl, plotUrl){
  #use best n and best K in testdata and check the performance
  tryCatch({
    final <- OzRsi(TestData, Sday = Bests, Lday = Bestl)
    buysell<-final$buysell
  }, error = function(e) {
    errormsg<<-conditionMessage(e)
  })

  #plot
  png(plotUrl,width = 1200, height = 600, units = "px")
  plotRSI(TestData, final)
  dev.off()
  
  result <- list(revenue = mean(rbind(final$r)[, 2]), buysell = buysell, final= final, Testdata = TestData)
}