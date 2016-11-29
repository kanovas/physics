package sample;

import java.util.ArrayList;

class System {

    ArrayList<Particle> currnetState;
    ArrayList<Particle> subState;
    final int PARTICLES_AMOUNT;

    System(int particlesAmount) {
        PARTICLES_AMOUNT = particlesAmount;
        initializeCurrentState();
    }

    private void initializeCurrentState() {

    }

    public ArrayList<Particle> getNextCondition() {
        subState = currnetState;
        for (int i = 0; i < PARTICLES_AMOUNT; i++)
            subState.set(i, countParticle(i));
        return currnetState;
    }

    private Particle countParticle(int i) {
        return new Particle(0,0,0);
    }

}
