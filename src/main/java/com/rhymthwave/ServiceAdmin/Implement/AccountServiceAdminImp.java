package com.rhymthwave.ServiceAdmin.Implement;

import com.rhymthwave.DAO.AccountDAO;
import com.rhymthwave.DAO.AuthorDAO;
import com.rhymthwave.DAO.ReportDAO;
import com.rhymthwave.DAO.RoleDAO;
import com.rhymthwave.DAO.WishlistDAO;
import com.rhymthwave.ServiceAdmin.IAccountServiceAdmin;
import com.rhymthwave.Utilities.SortBy;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Role;
import com.rhymthwave.entity.TypeEnum.EROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceAdminImp implements IAccountServiceAdmin{

	private final AccountDAO accountDAO;
	
	private final ReportDAO reportDAO;
	
	private final SortBy<String, String> sortService;
	
	private final WishlistDAO wishlistDAO;
	
	private final RoleDAO roleDao;
	
	private final AuthorDAO authorDAO;
	
	@Override
	public List<Account> findAllAccountByRole(Integer page, String sortBy, String sortField, EROLE role) {

		try {
//			Sort sort = sortService.sortBy(sortBy, sortField);
//
//			Pageable pageable = PageRequest.of(page, 6,sort);
			List<Account> pageAccount = accountDAO.findAllAccountRole(role);
			return pageAccount;
		} catch (Exception e) {
			log.error(">>>>>>>>>>> AccountServiceAdminImp:findAllAccountByRole | Error findAllAccount: {}",e.getMessage());
		return null;
		}
	}

	@Override
	public Account findById(String idUser) {
		Optional<Account> account = accountDAO.findById(idUser);
		if(account.isEmpty()) {
			return null;
		}
		return account.get();
	}

	@Override
	public int countReportByAccount(String idAccount) {
	
		return reportDAO.countReportByAccount(idAccount);
	}

	@Override
	public int countWithlistByAccount(String idAccount) {
	
		return wishlistDAO.countWishlistByAccount(idAccount);
	}

	@Override
	public void updateRoleStaff(String id) {
		Account account = accountDAO.findByEmail(id);
		Role role = roleDao.findByRole(EROLE.STAFF);
		Author author = new Author();
		author.setAccount(account);
		author.setRole(role);
		authorDAO.save(author);
	}

	@Override
	public void deleteRoleStaff(String id) {
		Account account = accountDAO.findByEmail(id);
		for (Author author : account.getAuthor()) {
			if(author.getRole().getRole() == EROLE.STAFF) {
				authorDAO.delete(author);
				break;
			}
		}
	}
}
