package it.unibo.deathnote.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote{
    private final List<Death> deaths = new LinkedList<>();
    private Death lastDeath;

    @Override
    public String getRule(final int ruleNumber) {
        if(ruleNumber<1 || ruleNumber >= RULES.size()){
            throw new IllegalArgumentException("This rule doesn't exist");
        }
        return RULES.get(ruleNumber-1);
    }

    @Override
    public void writeName(final String name) {
        Objects.requireNonNull(name);
        if(name.isBlank() || name.isEmpty()){
            throw new IllegalArgumentException("The argument can't be blank or empty");
        }
        Death death = new Death(name);
        this.lastDeath = death;
        this.deaths.add(death);
    }

    @Override
    public boolean writeDeathCause(final String cause) {
        if(this.lastDeath == null){
            throw new IllegalStateException("There's no name written");
        }
        return this.lastDeath.addDeathCause(cause);
    }

    @Override
    public boolean writeDetails(String details) {
        if(this.lastDeath == null){
            throw new IllegalStateException("There's no name written");
        }
        return this.lastDeath.addDetails(details);
    }

    @Override
    public String getDeathCause(String name) {
        for(Death d: this.deaths){
            if(d.getName().equals(name)){
                return d.getDeathCause();
            }
        }
        throw new IllegalArgumentException(name + " isn't on this note");
    }

    @Override
    public String getDeathDetails(String name) {
        for(Death d: this.deaths){
            if(d.getName().equals(name)){
                return d.getDetails();
            }
        }
        throw new IllegalArgumentException(name + " isn't on this note");
    }

    @Override
    public boolean isNameWritten(String name) {
        for(Death d: this.deaths){
            if(d.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private static final class Death {
        private static final int TIME_CAUSE = 40;
        private static final int TIME_DETAILS = 6000 + TIME_CAUSE;
        private static final String DEFAULT_CAUSE = "heart attack";
        private String name;
        private String deathCause;
        private String deathDetails;
        private long time;

        Death(final String name){
            this.name = name;
            this.deathCause = DEFAULT_CAUSE;
            this.deathDetails = "";
            this.time = System.currentTimeMillis();
        }
        
        String getName(){
            return this.name;
        }

        boolean addDeathCause(final String cause){
            if(cause == null){
                throw new IllegalArgumentException("The argument is null");
            }
            long timePassed = System.currentTimeMillis() - this.time;
            if(timePassed <= TIME_CAUSE){
                this.deathCause = cause;
                return true;
            }
            return false;
        }

        String getDeathCause(){
            return this.deathCause;
        }

        boolean addDetails(final String details){
            if(details == null){
                throw new IllegalArgumentException("The argument is null");
            }
            long timePassed = System.currentTimeMillis() - this.time;
            if(timePassed <= TIME_DETAILS){
                this.deathDetails = details;
                return true;
            }
            return false;
        }

        String getDetails(){
            return this.deathDetails;
        }
    }
    
}
