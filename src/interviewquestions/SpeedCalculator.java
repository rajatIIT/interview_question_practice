/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interviewquestions;

import java.util.ArrayList;

/**
 *
 * @author rajatpawar
 */
public class SpeedCalculator {
    
    public SpeedCalculator() {
        
    }
    /**
     * Question :
     * 
     * 
     * 
     * Problem Statement: In a Formula-1 challenge, there are n teams numbered 1 to n. Each team has a car and a driver. Car’s specification are as follows:
     * – Top speed: (150 + 10 * i) km per hour
– Acceleration: (2 * i) meter per second square.
– Handling factor (hf) = 0.8
– Nitro : Increases the speed to double or top speed, whichever is less. Can be used only once.

Here i is the team number.
The cars line up for the race. The start line for (i + 1)th car is 200 * i meters behind the ith car.

All of them start at the same time and try to attain their top speed. A re-assessment of the positions is done every 2 seconds(So even if the car has crossed the finish line in between, you’ll get to know after 2 seconds). 
* During this assessment, each driver checks if there is any car within 10 meters of his car, his speed reduces to: hf * (speed at that moment). 
* Also, if the driver notices that he is the last one on the race, he uses ‘nitro’.

Taking the number of teams and length of track as the input, Calculate the final speeds and the corresponding completion times.
     * 
     * 
     * 
     * 
     * 
     * @param num_cars
     * @param track_length 
     */
    
    float[] top_speed,acceleration,distance, current_speed,current_time,finishing_time;
    boolean[] raceCompleted,nitroUsed;
    
    int num_cars, time;
    float track_len;
    
    
    public void computeFinalSpeedAndTime(int num_car, int track_length){
        
        time=0;
        num_cars= num_car;
        track_len= track_length;
        // size of all arrays is num_cars
        
        // static lists
        top_speed = new float[num_cars];
        acceleration = new float[num_cars];
        finishing_time = new float[num_cars];
        
        // dynamic lists
        distance = new float[num_cars];
        nitroUsed = new boolean[num_cars];
        current_speed = new float[num_cars];
        current_time = new float[num_cars];
        raceCompleted = new boolean[num_cars];
        
        // initial starting position is different for each team member   
        // top speed is defined for each cae: the car cannot shoot this speed : take care
        
        for(int i=0;i<num_cars;i++)
        {
            raceCompleted[i]=false;
            nitroUsed[i]=false;
        }
        
        computeStartPositions();
        
        initializeTopSpeeds();
        // loop to compute positions every 2 seconds
        
        initializeAcclerations();
        
        
        // a while loop which exits when all the cars cross the finish line
        // implement two basic conditions : 
        // check if any car is within 10 meters, 
        // reduce to hf factor therein, decide if it is necessary to use nitro. 
        
        
        while (!raceComplete()) {
            
         
            System.out.println("-------------------------");
            System.out.println("Updating status at t=" + time);
            
            
            // and what is the distance covered by each car ()
            // distance covered by each car : distance covered in two seconds with the old initial speed, cureent speed
            // given acceleration 
            
            float distance_covered;
            
            System.out.println("-------------------------");
            System.out.println("Updating all distances.");
            for(int i=0;i<num_cars;i++){
                // update the distance covered
                // distance_covered (new) = distance_covered(old) + distance_covered( in two seconds)
                // lets assume that the speed is not updated 
                distance_covered = 2* current_speed[i];
                
                distance[i] = distance[i] + distance_covered;
                System.out.println("New Distance of " + (i+1) + " th car is " + distance[i]);
            }
            
            
            
            // asses situations at t=t
            // at time t=t , what is the speed attained by each car  (current speed of each car)
            // speed attained by each car : old speed + speed increaed following acceleration in t=2 seconds 
            // update speed for each car : 
            
            
            System.out.println("-------------------------");
            System.out.println("Updating all speeds.");
            float temp_speed;
            // lets update speed
            for(int i=0;i<num_cars;i++){
               temp_speed = current_speed[i] + (2* acceleration[i]);
               
               if (temp_speed<=top_speed[i])
               {current_speed[i]= temp_speed;}
               System.out.println("New speed of " + (i+1) + " th car is " + current_speed[i]);
            }
            
            
            
            
               // check if any car is within 10 meters ?? 
            
            System.out.println("-------------------------");
            System.out.println("Checking to see if there is any car within 10 meters");
            for(int i=0;i<num_cars;i++) {
                System.out.println("Looking up for " + (i+1) + " th car.");
                if (checkCarWithin10Mts(i)) {
                    // reduce car speed 
                    current_speed[i] = current_speed[i]*0.8f;
                    System.out.println("Reduced current car speed to " + current_speed[i]);
                  // break; 
                }
            }
            
            
            
            // check to see if nitro is required for any car ? 
            
           // check in the new assesed positions -> is the posiion less than all other positions ? 
            
            // lets assume that the driver is not last if some car is parallel to him
            
            System.out.println("-------------------------");
            System.out.println("Checking to see nitro is required for any car? ");
            
            for(int i=0;i<num_cars;i++){
            
                if (isDriverLast(i)){
                    System.out.println("Voila! " +i +  "th car is last !Lets use nitro!");
                   
                    if(!nitroUsed[i]) {
                    float double_speed;
                         double_speed = current_speed[i]*2.0f;
                    
                         if (double_speed > top_speed[i]) {
                             current_speed[i] = top_speed[i];
                         } else {
                             current_speed[i] = double_speed;
                         }
                        nitroUsed[i]=true; 
                   // current_speed[i] = current_speed[i]*2.0f;
                    } else {
                        System.out.println("Nitro was already used for " +i+ " th car.");
                    }
                }
                
            }
            
            
            updateCompletionStats();
            
            time=time+2;
            // update t 
        }
        
        
        System.out.println("---------------------");
        System.out.println("---------------------");
        
        System.out.println("Race complete in "+ time + " seconds.");
        System.out.println("---------------------");
        System.out.println("Final Speeds of various cars: ");
        
        for(int i=0;i<num_cars;i++)
        System.out.println("Speed of " + (i+1) + " th car is " + current_speed[i]);
        
        System.out.println("---------------------");
        System.out.println("Race completion times of various cars: ");
        for(int i=0;i<num_cars;i++)
        System.out.println("Race completion of " + (i+1) + " th car is " + finishing_time[i]);
        
        
        
    }

    private void computeStartPositions() {
        //  start place for ith car is 200*(i-1) meters behind the i-1 th car
        // start position of 1th car is 0 (lets say)
        // for each car, distance = (- 200*i)
        
         System.out.println("-----------------------------");
            System.out.println("Calculating start positions.");
        
            System.out.println("Initial start position for 1 th car is 0." );
            distance[0]=0;
        for(int i=1;i<num_cars;i++){
            distance[i] = distance[i-1] - (200*(i));
            System.out.println("Initial Start Distance for " + (i+1)  + " th car is " + distance[i] + ". ");
        }
        
        if (distance[num_cars-1]>((-1)* track_len)) {
            // works
        } else {
            System.out.println("All cars cannot come on the track !");
            System.exit(1);
        }
    }

    private void initializeTopSpeeds() {
         System.out.println("-----------------------------");
            System.out.println("Calculating top Speeds");
        for(int i=0;i<num_cars;i++){
            // for each car, – Top speed: (150 + 10 * i) km per hour
            // where i is the number of car from 1 to num_cars
           
            top_speed[i] =  (150 + (10*(i+1)));  // speed for car number i+1
            current_speed[i]=0.0f;
            System.out.println("The top speed for the " + (i+1) + "th car is" + top_speed[i]);
        }
        
    }

    private void initializeAcclerations() {
        
        //– Acceleration: (2 * i) meter per second square.
        System.out.println("-----------------------------");
            System.out.println("Calculating accelerations");
        for(int i=0;i<num_cars;i++){
          acceleration[i] = (2*(i+1));  
          System.out.println("The acceleration for the " + (i+1) + "th car is " + acceleration[i]);
        }
        
    }

    private boolean raceComplete() {
        // checks if the race is complete yet.
        
        for(int i=0;i<num_cars;i++){
            if (distance[i]<track_len)
                return false;
        }
        return true;
    }
    
    
    private boolean checkCarWithin10Mts(int currentCarnumber){
        for(int i=0;i<num_cars;i++){
            if (i!=currentCarnumber) {
            
            float currentDifference = distance[currentCarnumber] - distance[i];
        //    System.out.println("Difference " + currentDifference);
            
            float negDifference = (-1)*currentDifference;
            
            if ( currentDifference < 10 && currentDifference>0){
                // some car is within the distance
                System.out.println("car found within 10 meters.");
                System.out.println("Current car position: " + distance[currentCarnumber] + ". Position of car "+ (i+1) + " within 10 mts : " + distance[i]);
                return true;
            } else if (negDifference < 10 && negDifference>0) {
                System.out.println("car found within 10 meters.");
                System.out.println("Current car position: " + distance[currentCarnumber] + ". Position of car "+ (i+1) + " within 10 mts : " + distance[i]);
                return true;
            }
            }
         }
          return false;
    }

    private boolean isDriverLast(int carNo) {
        
        for(int i=0;i<num_cars;i++) {
            
            if (distance[i]<=distance[carNo]) {
            System.out.println("Car " + i + " found behind this car. Hence, Nitro not neeessary.");
                return false;
            }
        }
        
        return true;
    }

    private void updateCompletionStats() {
        
        
        for(int i=0;i<num_cars;i++){
            if (!raceCompleted[i]) {
                // check if the current distance completed is more than the track length
                if(distance[i]>track_len) {
                    // i has completed the race
                    raceCompleted[i] = true;
                    finishing_time[i]= time;
                }
                
            }
        }
        
    }
    
}
