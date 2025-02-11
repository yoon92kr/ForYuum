package com.foryuum.frontend.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.foryuum.frontend.common.exception.CommonException;

@Controller
@PropertySource("classpath:config/properties/linkage.properties")
public class FileUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
	
	private static String filePath;
	@Value("#{environment['common.filePath']}")
	private void setValue(String orgValue) {
		filePath = orgValue;
	}
	
	private static String acceptFileExtension;
	@Value("#{environment['accept.file.upload.extension']}")
	private void setAcceptFileExtension(String orgValue) {
		acceptFileExtension = orgValue;
	}
	
	private static String blockFileDownloadName;
	@Value("#{environment['block.file.download.name']}")
	private void setBlockFileDownloadName(String orgValue) {
		blockFileDownloadName = orgValue;
	}
	
	/**
	 * 파일 업로드
	 * 
	 * @param MultipartHttpServletRequest
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> fileUpload(MultipartHttpServletRequest mpRequest) throws CommonException {
		LOG.info("===========>fileUpload Start");
		Map<String, Object> resultData = new HashMap<String, Object>();
		List<String> realFileNameList = new ArrayList<String>();
		try {
			Iterator<String> iterator = mpRequest.getFileNames();
			MultipartFile multipartFile = null;
			String realFileName = null;
			String serverFilePath = filePath;

			Iterator<String> fileCheckIter = mpRequest.getFileNames();
			while(fileCheckIter.hasNext()) {
				multipartFile = mpRequest.getFile(fileCheckIter.next());

				// 파일 다운로드 Block 체크
				String fileExtension = getFileExtension(multipartFile.getOriginalFilename());
				String fileName = getFileName(multipartFile.getOriginalFilename());
				Map<String, Object> responseMsgVO = isFileUploadBlocked(fileName, fileExtension);
				if (responseMsgVO != null)
					return responseMsgVO;
			}

			File file = new File(serverFilePath);
			if(file.exists() == false) {
				file.mkdirs();
			}

			while(iterator.hasNext()) {
				multipartFile = mpRequest.getFile(iterator.next());
				String fileName = multipartFile.getOriginalFilename();
				if(multipartFile.isEmpty() == false) {
					realFileName = GlobalUtil.makeRealFileName(fileName);
					realFileNameList.add(realFileName);
					LOG.info("Upload File : {} ==> {}", fileName, serverFilePath + realFileName);
					String fullFilePath = serverFilePath;
					fullFilePath += replaceDirTraversal(realFileName);
					file = new File(fullFilePath);
					multipartFile.transferTo(file);
				}
			}
			resultData.put("realFileNameList", realFileNameList);
			resultData.put("msg", "success");
		} catch (Exception ex) {
			LOG.error("fileUpload Error :: {}", ex);
			resultData.put("msg", "error");
			return null;
		}
		LOG.info("fileUpload End <============");
		return resultData;
	}
	
	public static String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf(".");
		return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex).replaceAll("^\\.", "").trim();
	}

	public static String getFileName(String fileName) {
		int lastDotIndex = fileName.lastIndexOf(".");
		return lastDotIndex == -1 ? fileName : fileName.substring(0, lastDotIndex);
	}
	
	public static String replaceDirTraversal(String filePath) {
		return filePath.replace("\"", "")
				.replace("/", "")
				.replace("\\", "")
				.replace("..", "")
				.trim();
	}
	
	public static Map<String, Object> isFileUploadBlocked(String fileName, String fileExtension) {
		Map<String, Object> responseMsgVO = null;

		if(fileExtension.equals("")) {
			responseMsgVO = new HashMap<String, Object> ();
			responseMsgVO.put("isBlock", "T");
			responseMsgVO.put("msg", "notExistFileExtension");
			responseMsgVO.put("desc", "보안정책에 따라 확장자가 없는 파일은 업로드할 수 없습니다.");
			LOG.info("확장자가 없는 파일 업로드 시도. 파일명 >> {} / 파일확장자 >> {}", fileName, fileExtension);
			return responseMsgVO;
		}

		String isFileNameBlackList = checkFileNameBlackList(fileName);
		// 파일명에 허가되지 않은 단어가 포함되어있을 경우
		if (!isFileNameBlackList.equals("")) {
			responseMsgVO = new HashMap<String, Object> ();
			responseMsgVO.put("isBlock", "T");
			responseMsgVO.put("msg", "notAllowedFileName");

			String rspBlockedFileName = blockFileDownloadName.replace(",", " , ").trim();
			responseMsgVO.put("desc", "파일명에 업로드 불가능한 수 없는 문자가 포함되어 있습니다.\n( " + rspBlockedFileName + " ) \n 변경 후 재시도 해주십시오.");
			LOG.info("파일명에 허가되지 않은 문자가 존재함. 포함된 문자명 : \" " + isFileNameBlackList + " \" 파일명 >> {} / 파일확장자 >> {}", fileName, fileExtension);
			return responseMsgVO;
		}

		String isWhiteListFileExtension = checkFileExtensionWhitelist(fileExtension);
		// 허가 되지 않은 파일 확장자명 일 경우
		if (isWhiteListFileExtension.equals("")) {
			responseMsgVO = new HashMap<String, Object> ();
			responseMsgVO.put("isBlock", "T");
			responseMsgVO.put("msg", "notAllowedExtension");

			String rspAcceptFileExtension = acceptFileExtension.replace(",", ", ").trim();
			responseMsgVO.put("desc", "허용확장자(" + rspAcceptFileExtension + ") 외의 확장자는 보안정책에 따라 사용이 불가능합니다.\n변경 후 재시도 해주십시오.");
			LOG.info("허가되지 않은 파일확장자. 파일명 >> {} / 파일확장자 >> {}", fileName, fileExtension);
			return responseMsgVO;
		}

		return null;
	}
	
	/** 
	 * 다운로드 불가능한 파일 경로 리스트, 공백 return 시 저장이 가능한 파일명 그 외의 경우는 차단대상
	 * 
	 * @param String
	 * @return String
	 */
	public static String checkFileNameBlackList(String fileName){
		fileName = fileName.toLowerCase();
		String[] blockedFileArr = blockFileDownloadName.split(",");
		for (String blockedFile : blockedFileArr) {
			if (fileName.contains(blockedFile))
				return blockedFile;
		}
		return "";
	}
	
	/** 
	 * 업로드 가능한 파일 확장자 화이트리스트, 공백 return 시 미허가 확장자명
	 * 
	 * @param String
	 * @return String
	 */
	public static String checkFileExtensionWhitelist(String fileExtension) {
		fileExtension = fileExtension.toLowerCase();
		String[] acceptFileExtensionArr = acceptFileExtension.split(",");
		int idx = Arrays.asList(acceptFileExtensionArr).indexOf(fileExtension);
		String responseStr = idx == -1 ? "" : acceptFileExtensionArr[idx];
		return responseStr;
	}
	
	
}
