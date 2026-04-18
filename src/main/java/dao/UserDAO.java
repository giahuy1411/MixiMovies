package dao;

import java.util.List;

import entity.User;

public interface UserDAO {

	/** Truy vấn tất cả */
	List<User> findAll();

	/** Truy vấn tất cả với phân trang */
	List<User> findAll(int page, int size);

	/** Đếm tổng số user */
	long count();

	/** Truy vấn theo mã */
	User findById(String id);

	/** Tìm theo ID hoặc Email */
	User findByIdOrEmail(String idOrEmail);

	/** Thêm mới */
	void create(User item);

	/** Cập nhật */
	void update(User item);

	/** Cập nhật trạng thái hoạt động */
	void setActive(String id, boolean active);
}
