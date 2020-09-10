package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String idStr = req.getParameter("id");
        int musicID = Integer.parseInt(idStr);

        User user = (User)req.getSession().getAttribute("user");
        int user_id = user.getId();

        MusicDao musicDao = new MusicDao();
        boolean effect = musicDao.findMusicByMusicId(user_id, musicID);

        Map<String, Object> return_map = new HashMap<>();
        if (effect) {
            //以前这首歌被添加过喜欢,不能被重复添加
            return_map.put("msg", false);
        } else {
            boolean flg = musicDao.insertLoveMusic(user_id, musicID);
            if (flg) {
                return_map.put("msg", true);
            } else {
                return_map.put("msg", false);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(),return_map);


    }
}
