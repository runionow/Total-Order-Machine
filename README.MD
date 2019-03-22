# Total Order Machine

## Introduction

Total order machine has been developed to understand the concepts of total order multicast and broadcast, this is a plain vanilla implementation of Total Order Machine using UDP sockets and IS-IS total order algorithm 

ISIS total order takes an decentralized approach unlike using single sequencer at the central level for orchestrating the messages and is capable of handling both multicast and p2p messages

I have added some randomness to the project to test various scenarios (by adding unncessary sleeps while multicasting and recieving the messages)

## Solution

### Requirement

- Java 8
- IntelliJ IDE
- Maven

### Running the project 

As this project has been developed for pedagogy reason i recommend to open the project in an IntelliJ IDE or any maven supported IDE, as it helps to understand more about the concept of Total Order multicast

I am running the algorithm using three process where they exchange messages at random time intervals, you are allowed to modify the playground to test the capabilities of the program

the algorithm can be testedt by running the following java programs on intelliJIDE 


- Process1.java
- Process2.java
- Process3.java

**Dirty work-around** : Please start all the process under 7000ms (because i have used UDP sockets for broadcasting and they dont have any idea about the process on the group)

## Project Structure

## Assumptions

- I am assuming there are only three process(s) in the multicast group
- This project doesn't have capability to handle overlapping multicast groups assumes only single group
- Not fault tolerant
- Fixed set of process
- ISIS is capable of handling p2p messages 

## Algorithm - ISIS Algorithm

<img src="https://image.slidesharecdn.com/chapter11-141111075918-conversion-gate02/95/chapter-11-20-638.jpg?cb=1415693278" alt="Smiley face" >
<br>
<figcaption>This image had been taken from Distributed Systems: Concepts and Design</figcaption>
<br>
<br>

```$xslt
Process definition 
==================

- Every process has the same definition


Definition:

broadCastMessage(
              1. Message <data>
              2. counter value
              3. Process number
          )


HoldBackQueue(
            1. Message <data>
            2. Message_id aka (counter value)
            3. j (recieved from process_id)
            4. Sequence number
            5. i (current process_id)
            6. Status <Class> <Undeliverable>
        )

Status {
    Delivered,
    Undelivered
}


Initialization : 
    - sequence_number = 0 -> Upon recieving the broadcast message increment the sequence number
    - counter = 0 -> Upon sending the new message increment the counter 
    - process_id = manually assign some value to the host

a. Send a new message :
    1. Increment the counter
       counter = counter + 1;
    2. Broadcast message to all the other process 
          broadCastMessage(
              1. Message <data>
              2. counter
              3. process_id
          )

b. Upon recieving the broadCastMessage() Defintion from process 'j' :
    1. Increment the sequence number 
       sequence_number = sequence_number + 1;
    2. Send a replySequence(
            1. Message_id aka (counter value)
            2. current sequence_number
            3. process_id
        )
    3. Save Message to the 
        HoldBackQueue(
            1. Message <data>
            2. Message_id aka (counter value)
            3. j (recieved from process_id)
            4. Sequence number
            5. i (current process_id)
            6. Status <Undeliverable>
        )

c. On recieving the replySequence() 
    1. saveSequence(
        1. sequence_number
        2. process_id
    )

    2. upon recieving all the messages for that particular message_id
        - Choose the highest sequence_number in the list from the save sequence
        - Send to sendFinalSequence(
            1. Message_id aka (counter value)
            2. process_id (current)
            3. sequence_number
            4. proposed k value
        )

d. On delivering the message sendFinalSequence()
    - update sequence number
    - Modify <message_id aka counter value, process_id (current)> 
        task 1. Change proposed sequence number to new sequence number
        task 2. Change process suggested sequence number to k
        task 3. Change status to [Delivered] from [Undeliverable]

```

For more information regarding the algorithm please check the artifacts folder of this repository, i have included the details of the implementation of the algorithm

## Breaking the Ties

There is a possibility of ties while implementing this solution and handling carefully will result in optimal total order solution at the end

More information about the ties has been discussed in here.


#### Tie 1

```$xslt
 Choose smallest possible value for process_id if there are multiple suggesting the same sequence number
 while choosing the value with same sequence number
```


#### Tie 2 
```$xslt
Sort such that message with smallest sequence number is at the head of the delivery queue

If two sequence numbers are the same then
    place any undeliverable messages at the head
    to break further ties, place message with smallest suggesting process # at the head
end if

While message at head of queue has status deliverable do
    deliver the message at the head of the queue 
    remove this message from the queue
end while

```

## Results 

This is an exhaustive log for each process in the multicast group, for the sake of experiment i have added three logs to showcase the results of the solution 

#### Output at Process 1
```$xslt
"C:\Program Files\Java\jdk1.8.0_181\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\lib\idea_rt.jar=50218:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_181\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\rt.jar;D:\gitrepos\TotalOrderMain\target\classes;C:\Users\Arun Nekkalapudi\.m2\repository\org\zeromq\jeromq\0.5.1-SNAPSHOT\jeromq-0.5.1-20190219.022537-1.jar;C:\Users\Arun Nekkalapudi\.m2\repository\eu\neilalexander\jnacl\1.0.0\jnacl-1.0.0.jar" Process1
******************************
Welcome to Total-Order-Machine
******************************

This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.

[PROCESS_ID] : 25
[MESSAGE MULTICAST] : 2019-03-22 17:51:04.248 Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[MESSAGE MULTICAST] : 2019-03-22 17:51:04.248 Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:05.048 MessageR{sequence_no=1, message_uid='0137c284-66a6-47e9-97e4-c52d810cd54e', process_id=27}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:05.258 MessageR{sequence_no=1, message_uid='0137c284-66a6-47e9-97e4-c52d810cd54e', process_id=26}
All the feedback messages have been recieved
Propose Sequence No : MessageR{sequence_no=1, message_uid='0137c284-66a6-47e9-97e4-c52d810cd54e', process_id=26}
MessageS{message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', suggested_pid=26, suggested_sno=1}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', suggested_pid=26, suggested_sno=1}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}] 2
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}]
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:05.273 MessageR{sequence_no=2, message_uid='9477948c-cb28-47f3-8386-15e8a3a79fc9', process_id=27}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:05.98 MessageR{sequence_no=2, message_uid='9477948c-cb28-47f3-8386-15e8a3a79fc9', process_id=26}
All the feedback messages have been recieved
Propose Sequence No : MessageR{sequence_no=2, message_uid='9477948c-cb28-47f3-8386-15e8a3a79fc9', process_id=26}
MessageS{message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', suggested_pid=26, suggested_sno=2}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', suggested_pid=26, suggested_sno=2}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}] 2
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:07.142 Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=2, delivered=false}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=2, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', suggested_pid=25, suggested_sno=3}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}] 3
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
Delivered : Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:09.851 Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=3, delivered=false}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=3, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', suggested_pid=25, suggested_sno=4}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}] 4
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
Delivered : Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}
Delivered : Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}

```


#### Output at Process 2
```$xslt
"C:\Program Files\Java\jdk1.8.0_181\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\lib\idea_rt.jar=50223:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_181\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\rt.jar;D:\gitrepos\TotalOrderMain\target\classes;C:\Users\Arun Nekkalapudi\.m2\repository\org\zeromq\jeromq\0.5.1-SNAPSHOT\jeromq-0.5.1-20190219.022537-1.jar;C:\Users\Arun Nekkalapudi\.m2\repository\eu\neilalexander\jnacl\1.0.0\jnacl-1.0.0.jar" Process2
******************************
Welcome to Total-Order-Machine
******************************

This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.

[PROCESS_ID] : 26
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:04.599 Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:05.259 Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', suggested_pid=26, suggested_sno=1}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}] 2
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}]
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', suggested_pid=26, suggested_sno=2}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}] 2
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
[MESSAGE MULTICAST] : 2019-03-22 17:51:07.047 Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=2, delivered=false}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:08.02 MessageR{sequence_no=3, message_uid='5d4e3066-703a-4cb1-8458-f86fea5a35a4', process_id=27}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:08.12 MessageR{sequence_no=3, message_uid='5d4e3066-703a-4cb1-8458-f86fea5a35a4', process_id=25}
All the feedback messages have been recieved
Propose Sequence No : MessageR{sequence_no=3, message_uid='5d4e3066-703a-4cb1-8458-f86fea5a35a4', process_id=25}
MessageS{message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', suggested_pid=25, suggested_sno=3}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=2, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', suggested_pid=25, suggested_sno=3}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}] 3
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
Delivered : Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:09.851 Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=3, delivered=false}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=3, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', suggested_pid=25, suggested_sno=4}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}] 4
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
Delivered : Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}
Delivered : Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}

```


#### Output at Process 3

```$xslt
"C:\Program Files\Java\jdk1.8.0_181\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\lib\idea_rt.jar=50228:C:\Program Files\JetBrains\IntelliJ IDEA 2018.3.4\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_181\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_181\jre\lib\rt.jar;D:\gitrepos\TotalOrderMain\target\classes;C:\Users\Arun Nekkalapudi\.m2\repository\org\zeromq\jeromq\0.5.1-SNAPSHOT\jeromq-0.5.1-20190219.022537-1.jar;C:\Users\Arun Nekkalapudi\.m2\repository\eu\neilalexander\jnacl\1.0.0\jnacl-1.0.0.jar" Process3
******************************
Welcome to Total-Order-Machine
******************************

This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.

[PROCESS_ID] : 27
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:04.604 Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:05.047 Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', suggested_pid=26, suggested_sno=1}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}] 2
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}]
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=0, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', suggested_pid=26, suggested_sno=2}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}] 2
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
[NEW MESSAGE RECIEVED] : 2019-03-22 17:51:07.142 Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=2, delivered=false}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=2, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', suggested_pid=25, suggested_sno=3}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}] 3
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
Delivered : Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}
[MESSAGE MULTICAST] : 2019-03-22 17:51:09.317 Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=3, delivered=false}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:10.102 MessageR{sequence_no=4, message_uid='fec264bc-bf08-4fb4-91de-f180546d67e8', process_id=25}
[NEW REPLY MESSAGE RECIEVED] : 2019-03-22 17:51:10.767 MessageR{sequence_no=4, message_uid='fec264bc-bf08-4fb4-91de-f180546d67e8', process_id=26}
All the feedback messages have been recieved
Propose Sequence No : MessageR{sequence_no=4, message_uid='fec264bc-bf08-4fb4-91de-f180546d67e8', process_id=25}
MessageS{message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', suggested_pid=25, suggested_sno=4}
Final sequence has been received
[MESSAGE TO BE UPDATED]Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=3, delivered=false}
[FINAL_SEQUENCE] : MessageS{message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', suggested_pid=25, suggested_sno=4}
All table values : [Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}] 4
[Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}, Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}, Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}, Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}]
Delivered : Message{message='Hello', message_id='0137c284-66a6-47e9-97e4-c52d810cd54e', recieve_id=25, counter_num=2, sequence_num=1, delivered=true}
Delivered : Message{message='there', message_id='9477948c-cb28-47f3-8386-15e8a3a79fc9', recieve_id=25, counter_num=2, sequence_num=2, delivered=true}
Delivered : Message{message='World', message_id='5d4e3066-703a-4cb1-8458-f86fea5a35a4', recieve_id=26, counter_num=1, sequence_num=3, delivered=true}
Delivered : Message{message='Arun', message_id='fec264bc-bf08-4fb4-91de-f180546d67e8', recieve_id=27, counter_num=1, sequence_num=4, delivered=true}

```
This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.