package org.conan.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.conan.domain.BoardAttachVO;
import org.conan.domain.BoardVO;
import org.conan.domain.Criteria;
import org.conan.domain.PageDTO;
import org.conan.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {
	private BoardService service;
	
	@GetMapping("/list")
	public void list(Criteria cri ,Model model) {
		log.info("list" +cri);
		model.addAttribute("list",service.getList(cri));
		int total = service.getTotal(cri);
		log.info("total : "+total);
		model.addAttribute("pageMaker",new PageDTO(cri,total));
	}
	
	@PostMapping("/register")
	public String register(BoardVO board , RedirectAttributes rttr) {
		log.info("register 등록 : "+board);
		log.info("register 등록 aaaaaaaa: "+board.getBno());
		log.info("register 등록 aaaaaaaaaa: "+board.getAttachList());
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		service.register(board);
		rttr.addFlashAttribute("result",board.getBno());
		return "redirect:/board/list"; //하지 않는경우 새로 고침시 도배 
	}
	@GetMapping("/register")
	public void register() {
		
	}
	
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno ,@ModelAttribute("cri") Criteria cri, Model model) {
		log.info("/get or modify");
		model.addAttribute("board",service.get(bno));
	}
	
	@PostMapping("/modify")
	public String get(BoardVO board ,@ModelAttribute("cri") Criteria cri,RedirectAttributes rttr) {
		log.info("modify : " + board);
		if(service.modify(board)) {
			rttr.addFlashAttribute("result","success");
		}
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		rttr.addAttribute("type",cri.getType());
		rttr.addAttribute("keyword",cri.getKeyword());
		return "redirect:/board/list";
	}
	
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno,@ModelAttribute("cri") Criteria cri,RedirectAttributes rttr) {
		log.info("remove................." + bno);
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		if(service.remove(bno)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result","success");
		}
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		rttr.addAttribute("type",cri.getType());
		rttr.addAttribute("keyword",cri.getKeyword());
		return "redirect:/board/list";
	}
	


	@GetMapping(value = "/getAttachList" , produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		log.info("getAttachList......."+bno);
		return new ResponseEntity<>(service.getAttachList(bno),HttpStatus.OK);
	}
	
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {return;}
		log.info("delete attch files ");
		log.info(attachList);
		attachList.forEach(attach ->{
			try {
				Path file = Paths.get("c:/upload/"+attach.getUploadPath()+"/"+attach.getUuid()+"_"+attach.getFileName());
				Files.deleteIfExists(file);
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("c:/upload/"+attach.getUploadPath()+"/S_"+attach.getUuid()+"_"+attach.getFileName());
					Files.delete(thumbNail);
				}
			}catch(Exception e ) {log.error("delete file errer" + e.getMessage());}
		});
	}
	
}
