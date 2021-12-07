from django.db import models


class User(models.Model):
    username = models.CharField(max_length=50, null=False)
    email = models.CharField(max_length=255, null=False)
    name = models.CharField(max_length=100, null=False)
    city = models.CharField(max_length=50, null=True)
    phone_number = models.IntegerField(null=False)
    password = models.CharField(max_length=50, null=False)
    logged_in = models.BooleanField(null=False, default=False)
    mean_rate = models.IntegerField(null=True)

    def __str__(self):
        return self.username


class Review(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=False)
    reviewer = models.ManyToManyField(User, related_name='reviewer')
    rate = models.IntegerField(null=False)
    description = models.CharField(max_length=255, null=True)


class FollowedUser(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=False)
    followed = models.ManyToManyField(User, related_name='followed')


class Voivodeship(models.Model):
    VOIVODESHIPS_NAMES = [
        ('D', 'dolnośląskie'),
        ('C', 'kujawsko-pomorskie'),
        ('L', 'lubelskie'),
        ('F', 'lubuskie'),
        ('E', 'łódzkie'),
        ('K', 'małopolskie'),
        ('W', 'mazowieckie'),
        ('O', 'opolskie'),
        ('R', 'podkarpackie'),
        ('B', 'podlaskie'),
        ('G', 'pomorskie'),
        ('S', 'śląskie'),
        ('T', 'świętokrzyskie'),
        ('N', 'warmińsko-mazurskie'),
        ('P', 'wielkopolskie'),
        ('Z', 'zachodniopomorskie')
    ]
    name = models.CharField(max_length=30, null=False, choices=VOIVODESHIPS_NAMES)


class Category(models.Model):
    CATEGORIES_NAMES = [
        ('B', 'Książki'),
        ('C', 'Komiksy'),
        ('M', 'Czasopisma'),
        ('A', 'Audiobooki'),
        ('E', 'E-booki'),
        ('CD', 'Płyty CD'),
        ('W', 'Płyty winylowe')
    ]
    name = models.CharField(max_length=255, null=False, choices=CATEGORIES_NAMES)


class Status(models.Model):
    STATUS_NAMES = [
        ('N', 'Nowy'),
        ('U', 'Używany'),
        ('D', 'Uszkodzony'),
        ('E', 'Powystawowy')
    ]
    name = models.CharField(max_length=255, null=False, choices=STATUS_NAMES)


class Genre(models.Model):
    name = models.CharField(max_length=50, null=False)


class Announcement(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=False)
    title = models.CharField(max_length=100, null=False)
    description = models.CharField(max_length=255, null=False)
    voivodeship = models.ForeignKey(Voivodeship, on_delete=models.CASCADE, null=False)
    city = models.CharField(max_length=50, null=True)
    category = models.ForeignKey(Category, on_delete=models.CASCADE, null=False)
    status = models.ForeignKey(Status, on_delete=models.CASCADE, null=False)
    genre = models.ForeignKey(Genre, on_delete=models.CASCADE, null=False)
    year = models.IntegerField(null=True)
    negotiation = models.IntegerField(null=False)
    archived = models.BooleanField(null=False, default=False)
    purchaser_id = models.OneToOneField(User, on_delete=models.CASCADE, related_name='purchaser_id', null=True)
    image = models.ImageField(null=True)
    published_date = models.IntegerField(default=20210101, null=False)

    def __str__(self):
        return self.title


class Liked(models.Model):
    announcement = models.ManyToManyField(Announcement)
    user = models.ManyToManyField(User)




