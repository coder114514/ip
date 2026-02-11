package tobtahc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class ParserUtilTest {
    @Test
    public void findIndexAfterTokensTest() {
        assertEquals(6, ParserUtil.findIndexAfterTokens("ha hi bruh", 2));
    }

    @Test
    public void parseSwitches_slashAtTheEnd_nullReturned() {
        assertEquals(null, ParserUtil.parseSwitches("test /a /"));
    }

    @Test
    public void parseSwitches_valueGluedToSwitch_nullReturned() {
        assertEquals(null, ParserUtil.parseSwitches("do homework /by2025-2-3 16:00"));
    }

    @Test
    public void parseSwitches_noSpaceBeforeSlash_allowed() {
        var res = new TreeMap<String, String>();
        res.put("", "do homework");
        res.put("by", "2025-2-3 16:00");
        assertEquals(res, ParserUtil.parseSwitches("do homework/by 2025-2-3 16:00"));
    }

    @Test
    public void parseSwitches_valueDuplicateSwitches_nullReturned() {
        assertEquals(null, ParserUtil.parseSwitches("blah blah /A value1 /A value2"));
    }
}
