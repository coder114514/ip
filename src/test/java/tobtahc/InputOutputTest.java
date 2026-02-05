package tobtahc;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import tobtahc.command.CommandContext;
import tobtahc.command.CommandParseError;
import tobtahc.command.CommandParser;
import tobtahc.command.CommandResult;
import tobtahc.task.TaskList;
import tobtahc.util.ChanceAlwaysFalse;
import tobtahc.util.ChanceAlwaysTrue;

class InputOutputTest {
    @Test
    public void inputOutputTest() {
        var tasks = new TaskList();
        var alwaysTrue = new ChanceAlwaysTrue();
        var alwaysFalse = new ChanceAlwaysFalse();
        var ctx = new CommandContext(tasks, alwaysFalse);
        var ctxRev = new CommandContext(tasks, alwaysTrue);

        {
            var msg = List.of("Your tasks are all cleared.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("clear").execute(ctx)));
        }

        {
            var msg = List.of("echo: hi");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                    CommandParser.parse("hi").execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: read book",
                              "Now you have 1 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("todo read book").execute(ctx)));
        }

        {
            var msg = List.of("Task marked as done!",
                              "  [T][X] read book");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("mark 1").execute(ctx)));
        }

        {
            var msg = List.of("Dekramnu ksat!",
                              "  [T][ ] read book");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("unmark 1").execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: return book (by: Aug 05 2019 15:00)",
                              "Now you have 2 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("deadline return book /by 2019-08-05 15:00").execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)",
                              "Now you have 3 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("event project meeting /from 2019-08-06 14:00 /to 2019-08-06 16:00")
                                .execute(ctx)));
        }

        {
            var msg = List.of("Ti tog!",
                              "  dedda ksat: join sports club",
                              "Tsil eht ni sksat 4 evah uoy now.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("todo join sports club").execute(ctxRev)));
        }

        {
            var msg = List.of("Task marked as done!",
                              "  [T][X] join sports club");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("mark 4").execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: borrow book",
                              "Now you have 5 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("todo borrow book").execute(ctx)));
        }

        {
            var msg = List.of("You have 5 tasks in your list:",
                              "1.[T][ ] read book",
                              "2.[D][ ] return book (by: Aug 05 2019 15:00)",
                              "3.[E][ ] project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)",
                              "4.[T][X] join sports club",
                              "5.[T][ ] borrow book");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                        CommandParser.parse("list").execute(ctx)));
        }

        {
            var msg = List.of("2.[D][ ] return book (by: Aug 05 2019 15:00)");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                        CommandParser.parse("before or on 2019-08-06").execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: return book (by: Aug 09 2019 15:00)",
                              "Now you have 6 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("deadline return book/by 2019-08-09 15:00").execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: project meeting (from: Aug 12 2019 14:00 to: Aug 12 2019 16:00)",
                              "Now you have 7 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("event project meeting/To 2019-08-12 16:00/from 2019-08-12 14:00")
                                .execute(ctx)));
        }

        {
            var msg = List.of("Got it!",
                              "  task added: do homework (by: Aug 07 2019 20:00)",
                              "Now you have 8 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("deadline do homework /by 2019-08-07 20:00").execute(ctx)));
        }

        {
            var msg = List.of("7.[E][ ] project meeting (from: Aug 12 2019 14:00 to: Aug 12 2019 16:00)");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                        CommandParser.parse("occurs on 2019-08-12").execute(ctx)));
        }

        {
            var msg = List.of("You have 8 tasks in your list:",
                              "1.[T][ ] read book",
                              "2.[D][ ] return book (by: Aug 05 2019 15:00)",
                              "3.[E][ ] project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)",
                              "4.[T][X] join sports club",
                              "5.[T][ ] borrow book",
                              "6.[D][ ] return book (by: Aug 09 2019 15:00)",
                              "7.[E][ ] project meeting (from: Aug 12 2019 14:00 to: Aug 12 2019 16:00)",
                              "8.[D][ ] do homework (by: Aug 07 2019 20:00)");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                        CommandParser.parse("list").execute(ctx)));
        }

        {
            String[] msg = {"Syntax error! Correct syntax:", "  todo <task>"};
            var ex = assertThrows(CommandParseError.class, () ->
                    CommandParser.parse("todo").execute(ctx));
            assertArrayEquals(msg, ex.getLines());
        }

        {
            var msg = List.of("Task removed, but still UNDONE!",
                              "  [E][ ] project meeting (from: Aug 06 2019 14:00 to: Aug 06 2019 16:00)",
                              "Now you have 7 tasks in your list.");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, true),
                        CommandParser.parse("delete 3").execute(ctx)));
        }

        {
            var msg = List.of("You have 7 tasks in your list:",
                              "1.[T][ ] read book",
                              "2.[D][ ] return book (by: Aug 05 2019 15:00)",
                              "3.[T][X] join sports club",
                              "4.[T][ ] borrow book",
                              "5.[D][ ] return book (by: Aug 09 2019 15:00)",
                              "6.[E][ ] project meeting (from: Aug 12 2019 14:00 to: Aug 12 2019 16:00)",
                              "7.[D][ ] do homework (by: Aug 07 2019 20:00)");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                        CommandParser.parse("list").execute(ctx)));
        }

        {
            var msg = List.of("Here are the matching tasks in your list:",
                              "1.[T][ ] read book",
                              "2.[D][ ] return book (by: Aug 05 2019 15:00)",
                              "4.[T][ ] borrow book",
                              "5.[D][ ] return book (by: Aug 09 2019 15:00)");
            assertDoesNotThrow(() ->
                assertEquals(new CommandResult(msg, false, false),
                        CommandParser.parse("find book").execute(ctx)));
        }
    }
}
