from rest_framework import serializers
from .models import *


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = (
            'username',
            'email',
            'name',
            'city',
            'phone_number',
            'password',
            'logged_in',
            'mean_rate',
        )


class AnnouncementSerializer(serializers.ModelSerializer):
    user = serializers.ReadOnlyField(source='user.username')

    class Meta:
        model = Announcement
        fields = (
            'user',
            'title',
            'description',
            'voivodeship',
            'city',
            'category',
            'status',
            'genre',
            'year',
            'negotiation',
            'archived',
            'purchaser_id',
            'image',
            'published_date'
        )


class ReviewSerializer(serializers.ModelSerializer):
    class Meta:
        model = Review
        fields = (
            'user',
            'reviewer',
            'rate',
            'description'
        )


class FollowedUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = FollowedUser
        fields = (
            'user',
            'followed'
        )


class LikedSerializer(serializers.ModelSerializer):
    class Meta:
        model = Liked
        fields = (
            'announcement',
            'user'
        )


class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = (
            'name',
        )
