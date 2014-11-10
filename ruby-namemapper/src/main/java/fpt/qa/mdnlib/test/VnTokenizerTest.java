package fpt.qa.mdnlib.test;

import java.util.ArrayList;
import java.util.List;

import fpt.qa.mdnlib.nlp.vn.vntokenizer.VnTokenizer;
import fpt.qa.mdnlib.util.properties.Parameters;

public class VnTokenizerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		VnTokenizer.loadSpecialChars(Parameters.getSpecialcharsFile());
		VnTokenizer.loadRegexXMLFile(Parameters.getRegexFile());

		List strList = new ArrayList();
		
		strList.add("Sao tennis “dám chơi, dám chịu”. Ít nhất thì Serena và Nadal không đổ lỗi thất bại do... .. chấn thương như võ sỹ quyền Anh David Haye (thua Klitschko).");
        strList.add("“Học thầy không tày học… cha”. 4/8 tay vợt nữ lọt vào tứ kết được huấn luyện bởi chính bố đẻ của mình. Đó là Lisicki, Bartoli, Paszek và Pironkova.");
        strList.add("Cuộc thi Mister Vietnam 2010 được tổ chức thành hai giai đoạn: từ 12.11 - 12.12, tiếp nhận hồ sơ thí sinh dự thi và thi sơ kết chọn ra 30 thí sinh vào vòng chung kết; từ 15.12 - 1.1.2011 với vòng thi phụ tại Mũi Né và Vũng Tàu: Mr. Gentleman (Thanh lịch), Mr. Sport (Thể thao), Mr. Talent (Tài năng), Mr. Environment (Người đàn ông vì môi trường) và Người đàn ông kiến thức.");
        strList.add("Mất cân đối đã thành sự thật. Mất trí nhớ, mai một ký ức là một nguy cơ nhãn tiền. Không ai có thể ngăn lại quá trình vô cảm ấy nếu không còn tình yêu với lịch sử, với quá khứ mà không có nó, cuộc sống của chúng ta sẽ trở thành vô nghĩa. Xin các nhà làm quy hoạch, các nhà xây dựng và những người “yêu dự án” nhà cao tầng và công trình kiến trúc nguy nga, đồ sộ hãy suy nghĩ một phút với câu thơ rất hay của Chế Lan Viên: “Khi ta ở chỉ là nơi đất ở/ Khi ta đi đất đã hóa tâm hồn”. Đất đã hóa tâm hồn, xin cẩn trọng!");
        strList.add("Sau đó, ông Quốc đem bán lại cho ông Trần Dũng Trực (47 tuổi, ở TP Tuy Hòa, Phú Yên) giá từ 3 - 5 triệu đồng/cây tại rẫy. Ông Trực thuê nhân công ở H.Đông Hòa (Phú Yên) và phương tiện cơ giới ở TP Tuy Hòa lên khai thác được 15 cây. Tiếp đó, ông Trực thuê sáu xe đầu kéo chở 9 cây ra khỏi huyện thì bị cơ quan chức năng tạm giữ gần một tháng nay. Sáu cây còn lại (đã bứng gốc) hiện vẫn còn nằm trên đất rẫy.");
        strList.add("Đi kèm với chính sách này, S-Fone giảm giá lớn cho các dòng máy eCo S211C, V212C, C2823 và W362. Cụ thể, giá các dòng máy S211C, V212C và C2823 chỉ còn 230.000 đồng/máy so với giá ban đầu là 350.000 đồng đến 370.000 đồng/máy.");
        strList.add("Trong khi đó, phe đối lập Yemen ngày 10/7 đã kêu gọi leo thang các cuộc biểu tình trong khi Tổng thống nước này Ali Abdullah Saleh thừa nhận từ một quân y viện ở Arập Xêút rằng kế hoạch chuyển giao quyền lực của GCC mà ông từ chối ký kết là giải pháp cho tình trạng bất ổn chính trị ở nước ông./.");
        strList.add("Vòng bảng sẽ diễn ra từ ngày 26/11 – 10/12/2011. Các đội sẽ tham gia bốc thăm để chia thành 8 bảng và tranh tài theo vòng tròn tính điểm. Các lượt trận sẽ diễn ra ngẫu nhiên trên 01 trong 03 bản đồ: Giả Dụ Quan, Bàn Long Cốc Chiến và Ngũ Trượng Nguyên Chiến. Phần thưởng cho mỗi trận ở vòng bảng bao gồm: 10.000 đồng khóa, 100 vạn bạc khóa cho mỗi thành viên đội thắng và 5.000 đồng khóa, 50 vạn bạc khóa cho mỗi thành viên đội thua. Thời gian trao thưởng diễn ra từ ngày 19 – 21/12/2011.");
        strList.add("Người đẹp Mexico Perla Beltran (23 tuổi, cao 1m78) thành thạo 2 thứ tiếng: Anh và Tây Ban Nha. Cô tốt nghiệp đại học cách đây một năm và mong muốn trở thành nữ thương gia thành đạt trong tương lai.");
        strList.add("Đây luôn là điểm yếu của Nadal dù anh đã cố gắng để tạo nên đột biến bằng những quả giao bóng với độ xoáy lớn hơn kết hợp với việc tăng tốc độ lên khoảng 130 dặm/h (tương đương 209 km/h). Năm ngoái ở Mỹ mở rộng Nadal đã tiến bộ trông thấy trong giao bóng nhưng khi anh không duy trì được phong độ trong mùa giải năm nay, thất bại trước những người trả bóng xuất sắc như Djokovic cũng chẳng có gì lạ. (7)");
        strList.add("Sau khi Hà Nội ACB, Hà Nội T&T bị loại, chiều 16-1 đến lượt đại diện cuối cùng của bóng đá Hà Nội tại Cúp quốc gia là Hòa Phát Hà Nội cũng phải nói lời chia tay. Lẽ ra, Hòa Phát Hà Nội có thể vào vòng tứ kết nếu các chân sút sắc sảo hơn trong khâu dứt điểm.");
        strList.add("Băng đô các loại chun hoặc ren chun, giá rẻ , xinh xắn 95.000/ nơ email: abc@gmail.com");
        strList.add("Là chủ doanh nghiệp, đại biểu Phương Hữu Việt (Bắc Ninh) cho rằng “ngân hàng đang hạch toán hai sổ nên trần lãi suất 14%/năm nhưng lãi suất thực là 17-18%/năm. Quyết định hành chính làm méo mó hạch toán trong doanh nghiệp”. Ông Việt cũng đưa ra nhận xét “lòng tin của nhân dân giảm khi tiền không đưa vào sản xuất, lưu thông mà chủ yếu chuyển qua tích trữ vàng, USD, đất đai…”.");
        strList.add("Là chủ doanh nghiệp, đại biểu Phương Hữu Việt. Vòng bảng sẽ diễn ra từ ngày 26/11 – 10/12/2011.");
        
        for (int i = 0; i < strList.size(); i++) {
            String str = (String)strList.get(i);
            System.out.println("\n>>>" + str + "<<<");
            str = VnTokenizer.tokenize(str);
            System.out.println("\n>>>" + str + "<<<\n");
        }
	}
	
}
