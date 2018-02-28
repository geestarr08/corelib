/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.corelib.web.service;

import eu.europeana.corelib.definitions.db.entity.relational.ApiKey;
import eu.europeana.corelib.definitions.db.entity.relational.Token;
import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.web.email.EmailBuilder;
import eu.europeana.corelib.web.exception.EmailServiceException;
import eu.europeana.corelib.web.exception.ProblemType;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Willem-Jan Boogerd <www.eledge.net/contact>
 */
public abstract class EmailService {

    private static final Logger LOG = LogManager.getLogger(EmailService.class);

    @Resource
    private JavaMailSenderImpl mailSender;

    /**
     * Send an activation link to a new user account
     *
     * @param token   The token to send to the user
     * @param apiHost The api host url for the activation method in API
     */
    public void sendActivationToken(Token token, String apiHost) throws EmailServiceException {
        if ((token == null)
            || StringUtils.isBlank(token.getToken())
            || StringUtils.isBlank(token.getEmail())
            || StringUtils.isBlank(apiHost)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        String              url   = apiHost + "/user/activate/" + token.getEmail() + "/" + token.getToken();
        Map<String, Object> model = new HashMap<>();
        model.put("url", url);
        setAndSendMail(model, "activation", token.getEmail());
        LOG.info("Sent token ({}) and URL ({}) to {}", token.getToken(), url, token.getEmail());
    }

    /**
     * Send API keys to new api registration
     *
     * @param apiKey ApiKey object containing the new keys and email address
     */
    public void sendApiKeys(ApiKey apiKey) throws EmailServiceException {
        if (apiKey == null) {
            LOG.error("Problem with sendApiKeys: apiKey is null");
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("apiKey", apiKey);
        setAndSendMail(model, "apikeys", apiKey.getEmail());
        LOG.info("Sent API details to {}", apiKey.getEmail());
    }

    /**
     *
     * @param token   The token to send to the user
     * @param apiHost The api host url for the activation method in API
     * @throws EmailServiceException
     */
    public void sendNewPasswordToken(Token token, String apiHost, String salutation) throws EmailServiceException {
        if ((token == null)
            || StringUtils.isBlank(token.getToken())
            || StringUtils.isBlank(token.getEmail())
            || StringUtils.isBlank(apiHost)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        String url = apiHost + "/user/password/" + token.getEmail() + "/" + token.getToken();
        Map<String, Object> model = new HashMap<>();
        model.put("url", url);
        model.put("salutation", salutation);
        setAndSendMail(model, "forgotPassword", token.getEmail());
        LOG.info("Sent password reset url ({}) to {}", url, token.getEmail());
    }

    /**
     *
     * @param user to whom the confirmation is sent
     * @throws EmailServiceException
     */
    public void sendPasswordResetConfirmation(User user, String salutation) throws EmailServiceException {
        if ((user == null) || (user.getId() == null)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("salutation", salutation);
        setAndSendMail(model, "resetPasswordConfirm", user.getEmail());
        LOG.info("Sent password reset confirmation email to ({})", user.getEmail());
    }

    //TODO: remove because it's implemented in sedNewPasswordToken ?
    /**
     * Sends and email to user in case of forgotting password. It contains a link where the user can reset his password.
     *
     * @param user The user object
     * @param url  The URL of the password reset page
     */
    public void sendForgotPassword(final User user, final String url) throws EmailServiceException {
        if ((user == null) || (user.getId() == null) || StringUtils.isBlank(url)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        sendForgotPassword(user.getEmail(), url);
    }

    //TODO: remove because it's implemented in sedNewPasswordToken ?
    /**
     * Sends and email to user in case of forgotting password. It contains a link where the user can reset his password.
     *
     * @param email The user object
     * @param url   The URL of the password reset page
     */
    public void sendForgotPassword(final String email, final String url) throws EmailServiceException {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(url)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("url", url);
        setAndSendMail(model, "forgotPassword", email);
        LOG.info("Sent forgot password (URL={}) to {}", url, email);
    }

    /**
     * Sends the user's feedback to the site admin, and sends an thanking email to the user
     *
     * @param email    The user's email address
     * @param feedback The user's feedback
     */
    public void sendFeedback(String email, String feedback) throws EmailServiceException {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(feedback)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }
        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("feedback", feedback);

        // one email to organisation
        setAndSendMail(model, "userFeedback", null);

        // and one email to user
        setAndSendMail(model, "userFeedbackConfirm", email);
        LOG.info("Sent feedback of {}", email);
    }

    /**
     * Sends exception to the site admin
     */
    public void sendException(String subject, String body)
            throws EmailServiceException {
        if (StringUtils.isBlank(subject) || StringUtils.isBlank(body)) {
            throw new EmailServiceException(ProblemType.INVALIDARGUMENTS);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        model.put("body", body);

        // one email to organisation
        setAndSendMail(model, "exception", null);
    }

    /**
     * This method will be handled by Spring Framework.
     * No implementation needed
     *
     * @return a instance of EmailBuilder
     */
    protected abstract EmailBuilder createEmailBuilder();

    private void setAndSendMail(Map<String, Object> model, String template, String emailTo) throws
                                                                                            EmailServiceException {
        EmailBuilder builder = createEmailBuilder();
        builder.setModel(model);
        builder.setTemplate(template); // see corelib_web_emailConfigs
        if (null != emailTo) builder.setEmailTo(emailTo);
        mailSender.send(builder);
    }
}


