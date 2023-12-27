package com.rhymthwave.ServiceAdmin.Implement;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.ArtistDAO;
import com.rhymthwave.DAO.AuthorDAO;
import com.rhymthwave.DAO.RoleDAO;
import com.rhymthwave.Service.EmailService;
import com.rhymthwave.ServiceAdmin.IArtistService;
import com.rhymthwave.ServiceAdmin.INotification;
import com.rhymthwave.Utilities.SendMailTemplateService;
import com.rhymthwave.entity.Artist;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceAdminImp implements IArtistService, INotification<Artist> {

	private final ArtistDAO artistDAO;
	private final AuthorDAO authorDAO;
	private final AccountDAO accountDAO;
	private final RoleDAO roleDAO;
	private final EmailService mailService;
	private final SendMailTemplateService sendMailTemplateSer;
	public static final String TEMPLATE = "templateApproveRoles";
	
	@Override
	public Artist getOneArtistByEmail(String id) {
		Artist artist = artistDAO.findByEmail(id);
		if (artist == null) {
			return null;
		}

		return artist;
	}

	@Override
	public Object TotalAlbumAndSong(String idAccount) {

		return artistDAO.totalAlbumAndSong(idAccount);
	}

	@Override
	public Long sumListenedArtist(String idAccount) {
		Long sum = artistDAO.sumListenedArtist(idAccount);
		if (sum == null) {
			return 0L;
		}
		return sum;
	}

	@Override
	public int followerArtist(Integer idRole, String idAccount) {
		Author author = authorDAO.findAuthor(idRole, idAccount);

		return artistDAO.countFollowerArtist(author.getAuthorId());
	}

	@Override
	public List<Artist> getIsVerityArtist() {
		return artistDAO.findAllIsVerify(false);
	}

	@Override
	public Artist approveRolesArtist(Long idUser) {
		
		var artist = artistDAO.findById(idUser).orElse(null);
		if (artist != null) {
			var user = accountDAO.findByEmail(artist.getAccount().getEmail());
			var role = roleDAO.findById(2).orElse(null);
			var author = new Author();
			author.setAccount(user);
			author.setRole(role);
			authorDAO.save(author);

			artist.setIsVerify(true);
			artist.setActive(true);
			artistDAO.save(artist);
			
			sendNotification(artist, null);
		}

		return artist;
	}

	@Override
	public void sendNotification(Artist noti,String urlImg) {

		Email email = new Email();
		email.setFrom("nguyenkhoalolm@gmail.com");
		email.setTo(noti.getAccount().getEmail());
		email.setSubject("Congratulations");
		email.setBody(sendMailTemplateSer.getContentForArtist(noti.getArtistName(),TEMPLATE));
			mailService.enqueue(email);
		}


	@Override
	public void sendEmailWarring(String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEmailBan(String email, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEmailComfirmUser(String url, String email) {
		// TODO Auto-generated method stub
		
	}

}

