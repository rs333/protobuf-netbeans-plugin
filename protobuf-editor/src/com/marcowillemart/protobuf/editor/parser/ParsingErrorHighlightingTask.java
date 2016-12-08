package com.marcowillemart.protobuf.editor.parser;

import com.marcowillemart.common.lang.ParsingError;
import com.marcowillemart.common.util.Assert;
import com.marcowillemart.protobuf.editor.parser.ProtobufEditorParser.ProtobufEditorParserResult;
import java.util.LinkedList;
import java.util.List;
import javax.swing.text.Document;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;

/**
 * ParsingErrorHighlightingTask represents the task of highlighting Protobuf
 * parsing errors.
 *
 * @author mwi
 */
public final class ParsingErrorHighlightingTask
      extends ParserResultTask<ProtobufEditorParserResult> {

    private static final String LAYER_ID = "protobuf";
    private static final int PRIORITY = 100;

    /**
     * @effects Makes this be a new SyntaxErrorHighlightingTask.
     */
    ParsingErrorHighlightingTask() {
    }

    @Override
    public void run(ProtobufEditorParserResult result, SchedulerEvent event) {
        Assert.notNull(result);

        Document document = result.getSnapshot().getSource().getDocument(false);
        List<ErrorDescription> errorDescriptions = new LinkedList<>();

        for (ParsingError error : result.errors()) {
            errorDescriptions.add(
                    ErrorDescriptionFactory.createErrorDescription(
                            Severity.ERROR,
                            error.message(),
                            document,
                            error.line()));
        }

        HintsController.setErrors(document, LAYER_ID, errorDescriptions);
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }
}
