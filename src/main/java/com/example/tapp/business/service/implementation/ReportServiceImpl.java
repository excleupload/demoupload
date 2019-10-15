package com.example.tapp.business.service.implementation;

import static com.example.tapp.ws.common.response.ProcessorResponseUtils.success;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.WebSocketSession;

import com.example.tapp.business.service.INotification;
import com.example.tapp.business.service.MessageService;
import com.example.tapp.business.service.ReportService;
import com.example.tapp.business.service.UserConnectionService;
import com.example.tapp.common.discriminator.MessageType;
import com.example.tapp.common.discriminator.ReportStatus;
import com.example.tapp.common.dto.MessageDialogDto;
import com.example.tapp.common.dto.MessageDto;
import com.example.tapp.common.dto.ReportDto;
import com.example.tapp.common.list.helper.Order;
import com.example.tapp.common.list.helper.Page;
import com.example.tapp.common.list.helper.PageResponse;
import com.example.tapp.common.list.helper.Sort;
import com.example.tapp.common.utils.AppUtils;
import com.example.tapp.common.utils.KeyUtils;
import com.example.tapp.data.dao.ReportDao;
import com.example.tapp.data.dao.UserConnectionDao;
import com.example.tapp.data.dao.UserDao;
import com.example.tapp.data.entities.Report;
import com.example.tapp.data.entities.User;
import com.example.tapp.data.entities.UserConnection;
import com.example.tapp.data.exception.GeneralException;
import com.example.tapp.data.exception.RecordNotFoundException;
import com.example.tapp.data.exception.UserNotFoundException;
import com.example.tapp.ws.common.request.PacketName;
import com.example.tapp.ws.manager.SessionManager; 
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private com.example.tapp.common.utils.FileHandler fileHandler;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserConnectionDao connDao;

    @Autowired
    private MessageService messageService;

    @Autowired
    private INotification iNotification;

    @Autowired
    private UserConnectionService userConnService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HashMap<String, String> saveReportFile(MultipartFile file) throws GeneralException {

        try {
            log.info("Report file '" + file.getOriginalFilename() + "' processing.");
            String fileName = fileHandler.saveReportFile(file.getBytes(), file.getOriginalFilename());
            log.info("Report file '" + file.getOriginalFilename() + "' processed.");

            HashMap<String, String> fileResponse = new HashMap<>();
            fileResponse.put(KeyUtils.VIEW_FILE_NAME, file.getOriginalFilename());
            fileResponse.put(KeyUtils.SERVER_FILE_NAME, fileName);
            return fileResponse;
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    @Override
    public ReportDto saveReport(MultipartFile file, ReportDto dto) throws GeneralException {
        Report report = new Report();
        try {
            report.setReporterUser(userDao.getUserById(dto.getReporterId()));
            report.setReportedUser(userDao.getUserById(dto.getReportedId()));
        } catch (UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
        report.setReason(dto.getReason());
        if (file != null) {
            HashMap<String, String> fileInfo = this.saveReportFile(file);
            report.setProofImage(AppUtils.jsonStringify(fileInfo));
        }
        report.setStatus(ReportStatus.PENDING);
        report.setIsRead(false);
        return reportDao.save(report).dto();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeReport(UUID reportId) throws GeneralException {
        try {
            Report report = reportDao.getById(reportId);
            if (report.getProofImage() != null && !report.getProofImage().isEmpty()) {
                try {
                    HashMap<String, String> files = AppUtils.jsonParse(report.getProofImage(), HashMap.class);
                    fileHandler.deleteReportFile(files.get(KeyUtils.SERVER_FILE_NAME));
                } catch (IOException e) {
                    log.info(e.getMessage());
                }
            }
            reportDao.delete(report);
        } catch (RecordNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    @Override
    public ReportDto getReportById(UUID reportId) throws GeneralException {
        try {
            return reportDao.getById(reportId).dto();
        } catch (RecordNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    @Override
    public ReportDto update(ReportDto dto) throws GeneralException {
        try {
            Report report = reportDao.getById(dto.getId());
            if (report.getStatus().equals(ReportStatus.PROCESSED))
                throw new GeneralException("This report already processed.");

            report.setReportedUser(userDao.getUserById(dto.getReportedId()));

            report.setReason(dto.getReason());
            if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
                report.setProofImage((dto.getFiles() == null || dto.getFiles().isEmpty()) ? null
                        : AppUtils.jsonStringify(dto.getFiles()));
            }
            return reportDao.update(report).dto();
        } catch (RecordNotFoundException | UserNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] getReportFile(UUID reportId) {
        Object[] objects = new Object[2];
        try {
            Report report = reportDao.getById(reportId);
            if (report.getProofImage() != null && !report.getProofImage().isEmpty()) {
                HashMap<String, String> files = AppUtils.jsonParse(report.getProofImage(), HashMap.class);
                objects[0] = fileHandler.getReportFile(files.get(KeyUtils.SERVER_FILE_NAME));
                objects[1] = files.get(KeyUtils.VIEW_FILE_NAME);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        if (objects[0] == null) {
            objects[0] = fileHandler.getDefaultNoImage();
            objects[1] = fileHandler.NO_IMAGE;
        }
        return objects;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageResponse<?> getReportListByReporter(UUID reporterId, HashMap<String, Object> options) {
        Page page = (Page) options.get(Page.PAGE);
        Sort sort = Sort.of(Order.DESC, Report.CREATED_ON);
        Object[] objects = reportDao.getListByReporter(reporterId, page, sort);
        PageResponse<ReportDto> pageResponse = new PageResponse<>();
        pageResponse.setTotal((long) objects[1], page);
        pageResponse.setList(((List<Report>) objects[0]).stream().map(Report::dto).collect(Collectors.toList()));
        return pageResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageResponse<?> getReportListByReporter(HashMap<String, Object> options) {
        Sort sort = Sort.of(Order.DESC, Report.CREATED_ON);
        Object[] objects = reportDao.getListByReporter(sort);
        PageResponse<ReportDto> pageResponse = new PageResponse<>();
        pageResponse.setList(((List<Report>) objects[0]).stream().map(Report::dto).collect(Collectors.toList()));
        return pageResponse;
    }

    @Override
    public ReportDto adminReportUpdate(ReportDto dto, UUID reportId) throws GeneralException {
        try {
            Report report = reportDao.getById(reportId);
            report.setStatus(ReportStatus.PROCESSED);
            report.setIsRead(true);
            report.setReplyAdmin(dto.getReplyAdmin());
            this.sendSystemMessage(report);
            return reportDao.update(report).dto();
        } catch (RecordNotFoundException e) {
            log.info(e.getMessage());
            throw new GeneralException(e.getMessage());
        }
    }

    private void sendSystemMessage(Report report) {
        User sysUser = userDao.getSystemUser();
        User receiver = report.getReporterUser();
        try {
            UserConnection connection = connDao.getByOwnerAndConnectedUser(sysUser.getId(), receiver.getId());
            UUID dialogId = null;
            if (connection.getMessageDialogId() == null) {
                MessageDialogDto dialogDto = new MessageDialogDto();
                dialogDto.setOccupantIds(Arrays.asList(sysUser.getId(), receiver.getId()));
                dialogId = messageService.create(dialogDto).getDialogId();
            } else {
                dialogId = connection.getMessageDialogId();
            }
            MessageDto messageDto = new MessageDto();
            messageDto.setDialogId(dialogId);
            messageDto.setType(MessageType.TEXT);
            messageDto.setMessage(report.getReplyAdmin());
            messageDto.setSender(sysUser.getId());
            messageDto = messageService.addMessage(messageDto);

            /** Send Message to receiver **/
            WebSocketSession receiverSession = SessionManager.getUserSession(messageDto.getReceiver());
            if (receiverSession != null && receiverSession.isOpen()) {
                Object[] notifyData = userConnService.getConnByOwnerUserAndConnectedUser(messageDto.getSender(),
                        messageDto.getReceiver());
                iNotification.onMessage(notifyData, messageDto);
                success(receiverSession, PacketName.MESSAGE, messageDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error {}", e.getMessage());
        }
    }

    public List<?> getlist() {
        String query = "select reason,created_on,reported_user,reporter_user,proof_image,status  from report order by created_on DESC";
        try {
            List<?> userSupport = returnResultSet(query);
            return userSupport;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object[]> getUserSupportCount() {
        return reportDao.getUserSupportCount();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public PageResponse<?> getReportListByNotification(UUID reporterId) {

        Object[] objects = reportDao.getListByAdminnotificatonReply(reporterId);
        Query query = entityManager.createQuery("update  Report u set u.isRead=:is_read  where u.id= :reporterId");
        query.setParameter("is_read", true);
        query.setParameter("reporterId", reporterId);
        query.executeUpdate();
        PageResponse<ReportDto> pageResponse = new PageResponse<>();
        pageResponse.setList(((List<Report>) objects[0]).stream().map(Report::dto).collect(Collectors.toList()));
        return pageResponse;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResponse<?> getUserReply() {
        Object[] objects = reportDao.getListByReplyStatus();
        PageResponse<ReportDto> pageResponse = new PageResponse<>();
        pageResponse.setTotal((long) objects[1]);
        pageResponse.setList(((List<Report>) objects[0]).stream().map(Report::dto).collect(Collectors.toList()));
        return pageResponse;
    }

    public List<?> returnResultSet(@NotNull String query) throws Exception {
        if (query.trim().length() == 0)
            throw new IllegalArgumentException("Sql Query Should Not Empty");
        javax.persistence.Query queryObj = em.createNativeQuery(query);
        if (log.isDebugEnabled())
            log.debug("{}", query);
        return queryObj.getResultList();
    }

}