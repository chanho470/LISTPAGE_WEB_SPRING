package org.conan.test;

import java.util.List;
import java.util.stream.IntStream;

import org.conan.domain.BoardVO;
import org.conan.domain.Criteria;
import org.conan.domain.ReplyVO;
import org.conan.persistence.BoardMapper;
import org.conan.mapper.ReplyMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j

public class BoardMapperTest {
	@Setter(onMethod = @__({@Autowired}))
	private BoardMapper mapper;
	
	@Test
	public void testGetList() {
		mapper.getList().forEach(board -> log.info(board));
	}
	@Test
	public void testInsert() {
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		mapper.insert(board);
		log.info(board);
	}
	@Test
	public void testRead() {
		BoardVO board = mapper.read(1L);
		log.info(board);
	}
	
	@Test
	public void testDelete() {
		log.info("DELETE COUNT :" + mapper.delete(9L));
	}
	@Test
	public void testUpdate() {
		BoardVO board = new BoardVO();
		board.setBno(2L);
		board.setTitle("수정한 제목");
		board.setContent("수정한 내용");
		board.setWriter("rose");
		int count = mapper.update(board);
		log.info("Update Count : " + count);
	}
	@Test
	public void testInsertSelectKey() {
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		mapper.insertSelectKey(board);
		log.info(board);
	}
	@Test
	public void testPaging() {
		Criteria cri = new Criteria(4,3);
		List<BoardVO> list = mapper.getListWithPaging(cri);
		list.forEach(board ->log.info(board));
	}
	
	@Test
	public void testSearch() {
		Criteria cri = new Criteria();
		cri.setKeyword("찬호");
		cri.setType("TC");
		List<BoardVO> list = mapper.getListWithPaging(cri);
		list.forEach(board ->log.info(board));
	}
	@Test
	public void testMapper() {
		log.info("나왔니? ......."+mapper);
	}
	
	
}
