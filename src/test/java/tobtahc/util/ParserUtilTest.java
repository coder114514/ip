package tobtahc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import tobtahc.util.ParserUtil;

public class ParserUtilTest {
    @Test
    public void indexAfterTokensTest() {
        assertEquals(ParserUtil.indexAfterTokens("ha hi bruh", 2), 6);
    }

    @Test
    public void parseSwitches_slashAtTheEnd_nullReturned() {
        assertEquals(ParserUtil.parseSwitches("test /a /"), null);
    }

    @Test
    public void parseSwitches_valueGluedToSwitch_nullReturned() {
        assertEquals(ParserUtil.parseSwitches("do homework /by2025-2-3 16:00"), null);
    }

    @Test
    public void parseSwitches_noSpaceBeforeSlash_allowed() {
        var res = new TreeMap<String, String>();
        res.put("", "do homework");
        res.put("by", "2025-2-3 16:00");
        assertEquals(ParserUtil.parseSwitches("do homework/by 2025-2-3 16:00"), res);
    }

    @Test
    public void parseSwitches_valueDuplicateSwitches_nullReturned() {
        assertEquals(ParserUtil.parseSwitches("blah blah /A value1 /A value2"), null);
    }
}
