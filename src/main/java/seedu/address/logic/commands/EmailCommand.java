package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_BODY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_SERVICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_TO;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.email.Body;
import seedu.address.model.email.Service;
import seedu.address.model.email.Subject;
import seedu.address.model.person.PersonContainsTagPredicate;

/**
 * Emails all people with a particular tag either using gmail/outlook
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String MESSAGE_EMAIL_SENT = "Email .";
    public static final String MESSAGE_NOT_SENT = "Please enter a valid name/tag with a valid Email ID.";
    public static final String EMAIL_SERVICE_GMAIL = "gmail";
    public static final String EMAIL_SERVICE_OUTLOOK = "outlook";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":  people in the Address Book.\n"
            + "The 'to' field is compulsory\n"
            + "The 'to' field can take tag and it only supports one parameter.\n"
            + "Parameters: "
            + PREFIX_EMAIL_SERVICE + "SERVICE "
            + PREFIX_EMAIL_TO + "TAGS "
            + PREFIX_EMAIL_SUBJECT + "SUBJECT "
            + PREFIX_EMAIL_BODY + "BODY \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EMAIL_SERVICE + "gmail"
            + PREFIX_EMAIL_TO + "cs2103"
            + PREFIX_EMAIL_SUBJECT + "Meeting "
            + PREFIX_EMAIL_BODY + "On Monday ";

    private final PersonContainsTagPredicate predicate;
    private final Subject subject;
    private final Body body;
    private final Service service;

    public EmailCommand(PersonContainsTagPredicate predicate, Subject subject, Body body, Service service) {
        this.predicate = predicate;
        this.subject = subject;
        this.body = body;
        this.service = service;

    }

    /**
     * Calls event for opening the url in browser panel
     * @param recipients String of all recipients
     * @throws ParseException when wrong recipients
     */
    public void processEmail(String recipients) throws ParseException {
        requireNonNull(recipients);

        if (recipients.equals("")) {
            throw new ParseException("Invalid recipients");
        }
        model.processEmailEvent(recipients, subject, body, service);
    }

    /**
     * Checks if service is one of the offered service of "gmail" or "outlook"
     * @param service mentioned by the user
     * @throws ParseException if not an offered service
     */
    public void checkServiceValid(Service service) throws ParseException {
        if (!service.service.equalsIgnoreCase(EMAIL_SERVICE_GMAIL)
                && !service.service.equalsIgnoreCase(EMAIL_SERVICE_OUTLOOK)) {
            throw new ParseException("Invalid service");
        }
    }


    @Override
    public CommandResult execute() {


        try {
            checkServiceValid(service);
            String emailTo = model.createEmailRecipients(predicate);
            processEmail(emailTo);
        } catch (ParseException e) {
            e.printStackTrace();
            return new CommandResult(MESSAGE_NOT_SENT);
        }
        return new CommandResult(MESSAGE_EMAIL_SENT);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && this.predicate.equals(((EmailCommand) other).predicate)); // state check
    }
}
