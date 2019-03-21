# Total Order Machine

## Introduction

Total order machine has been developed to understand the concepts of total order multicast and broadcast

## Solution

### Requirement

- Java 8
- IntelliJ IDE
- Maven

### Running the project 

As this project has been developed for pedagogy reason i recommend to open the project in an IntelliJ IDE or any maven supported IDE, as it helps to understand more about the concept of Total Order multicast

I am running the algorithm using three process where they exchange messages at random time intervals, you are allowed to modify the playground to test the capabilities of the program

You can test by running 

- Process1.java
- Process2.java
- Process3.java


## Project Structure

## Assumptions

- I am assuming there are only three projects in the multicast group
- This project doesn't have capability to handle overlapping multicast groups assumes only single group
- Not fault tolerant

## Algorithm - ISIS Algorithm

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


## Results 

```$xslt
Sample output goes here
```

This project has been done as a part of Distributed Systems coursework
(C) 2019 Arun Nekkalapudi <anekkal@iu.edu>, Indiana University Bloomington.