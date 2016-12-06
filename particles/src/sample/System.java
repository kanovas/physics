package sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


class System {

    private static final double DELTA = 0.5;
    private static final Long TICK_TIME = 100L;
    private ArrayList<Particle> currnetState;
    private ArrayList<Particle> subState;
    private final int PARTICLES_AMOUNT;
    private boolean condition;

    System(int particlesAmount) {
        PARTICLES_AMOUNT = particlesAmount;
        currnetState = new ArrayList<>();
        condition = true;
        initializeCurrentState();
    }


    private void initializeCurrentState() {
        for (int i = 0; i < PARTICLES_AMOUNT; i++) {
            //TODO : fill currentState
            currnetState.add(new Particle(0, 0, 0));
        }
    }

    ArrayList<Particle> getNextCondition() {
        Long startTime = getCurrentTime();
        subState = currnetState;
        currnetState = new ArrayList<>();
        for (int i = 0; i < PARTICLES_AMOUNT; i++)
            currnetState.add(countParticle(i));
        setIsWorkingCondition();
        correctTickTime(startTime);
        return currnetState;
    }

    private Particle countParticle(int i) {
        //TODO : count Particle position according to subState and currentState
        return new Particle(0, 0, 0);
    }

    private void setIsWorkingCondition() {
        condition = false;
        for (int i = 0; i < PARTICLES_AMOUNT; i++) {
            if (subState.get(i).getDistanceTo(currnetState.get(i)) > DELTA) {
                condition = true;
                return;
            }
        }
    }

    private void correctTickTime(Long startTime) {
        Long workingTime = getCurrentTime() - startTime;
        if (workingTime < TICK_TIME) {
            try {
                TimeUnit.SECONDS.sleep((TICK_TIME - workingTime) / 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Long getCurrentTime() {
        Date date = new Date();
        return date.getTime();
    }

    boolean isWorking() {
        return condition;
    }
}
