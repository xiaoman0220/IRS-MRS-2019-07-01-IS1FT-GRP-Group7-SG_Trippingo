

# Create your models here.

class Recommendation:

	def __init__(self, attraction_id, name, description, img_url):
		self.attraction_id = attraction_id
		self.name = name
		self.description = description
		self.img_url = img_url
		self.selected = False
