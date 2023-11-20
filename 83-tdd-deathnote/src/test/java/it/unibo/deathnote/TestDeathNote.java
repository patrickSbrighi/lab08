package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {
    private DeathNote book;
    private static final String FIRST_PERSON = "Ernesto Bianchi";
    private static final String SECOND_PERSON = "Patrick Sbrighi";
    private static final int OUT_OF_TIME_CAUSE = 100;
    private static final int OUT_OF_TIME_DETAILS = 6000 + OUT_OF_TIME_CAUSE;
    /**
     * Prepare the tests
     */
    @BeforeEach
    void setUp(){
        book = new DeathNoteImpl();
    }

    @Test
    void testRangeRules(){
        final var illegalRules = List.of(-1,0,DeathNote.RULES.size()+1);
        for(int i: illegalRules){
            try{
                book.getRule(i);
                fail("Illegal index allowed");
            }catch(Exception e){
                assertTrue(e instanceof IllegalArgumentException);
                assertNotNull(e);
                assertFalse(e.getMessage().isBlank());
            }
        }
    }

    @Test
    void testNoRuleEmpty(){
        for(int i = 1;i<DeathNote.RULES.size();i++){
            final String rule = book.getRule(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    void testWriteDeathNote(){
        final String empty = "";
        assertFalse(book.isNameWritten(FIRST_PERSON));
        book.writeName(FIRST_PERSON);
        assertTrue(book.isNameWritten(FIRST_PERSON));
        assertFalse(book.isNameWritten(SECOND_PERSON));
        assertFalse(book.isNameWritten(empty));
    }

    @Test
    void testWriteDeathCause() throws InterruptedException{
        try{
        book.writeDeathCause("Suicide");
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
        book.writeName(SECOND_PERSON);
        assertEquals("heart attack", book.getDeathCause(SECOND_PERSON));
        book.writeName(FIRST_PERSON);
        assertTrue(book.writeDeathCause("karting accident"));
        Thread.sleep(OUT_OF_TIME_CAUSE);
        assertFalse(book.writeDeathCause("car accident"));
        assertEquals("karting accident", book.getDeathCause(FIRST_PERSON));
    }

    @Test
    void testDeathDetails() throws InterruptedException{
        try{
            book.writeDetails("drug overdose");
        }catch(Exception e){
            assertTrue(e instanceof IllegalStateException);
        }
        book.writeName(FIRST_PERSON);
        assertTrue(book.getDeathDetails(FIRST_PERSON).isEmpty());
        assertTrue(book.writeDetails("ran for too long"));
        book.writeName(SECOND_PERSON);
        Thread.sleep(OUT_OF_TIME_DETAILS);
        assertFalse(book.writeDetails("overdose"));
        assertTrue(book.getDeathDetails(SECOND_PERSON).isEmpty());
    }
}