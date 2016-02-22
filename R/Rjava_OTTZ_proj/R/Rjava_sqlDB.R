getStockDB<-function(conn,start,end,symbol){
  #split datetime,  '2013-08-01 08:30:00' to '2013-08-01' & '08:30:00'
  start_split <- splitDatetime(start)
  end_split <- splitDatetime(end)
  
  sqlString <- paste("with temp as(
                    SELECT convert(datetime,convert(char(11),Date)+convert(char(8),ImportTime)) as itime,Symbol,Price,Date,ImportTime
                    FROM Stock AS A WITH (nolock)
                    Where (Date between '",start_split$date,"' and '",end_split$date,"') and Symbol = '",symbol,"'
                    )                    
                    SELECT itime,Symbol,Price
                    from temp
                    Where itime BETWEEN '",start,"' AND '",end,"'
                    order by itime",sep="")
  
  sqldata <- sqlQuery(conn, sqlString)
  TrainData <- data.frame(DateTime = sqldata[ ,1], v2 = NA, v3 = NA, v4 = NA, Close = sqldata[ ,3])
  TrainData$DateTime <- as.POSIXlt(TrainData$DateTime)
  TrainData
}

buildConnection <- function(odbcName, userName, pwd){
  conn <- odbcConnect(odbcName,uid=userName,pwd=pwd)
}

#this private function split datetime by space
splitDatetime <- function(datetime){ 
  datetime_list <- strsplit(datetime," ")[[1]]
  datetime_split<-list(date=datetime_list[1], time=datetime_list[2])
}