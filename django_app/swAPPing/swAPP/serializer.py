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

    def create(self, validated_data):
        return User.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.username = validated_data.get('username', instance.username)
        instance.email = validated_data.get('email', instance.email)
        instance.name = validated_data.get('name', instance.name)
        instance.city = validated_data.get('city', instance.city)
        instance.phone_number = validated_data.get('phone_number', instance.phone_number)
        instance.password = validated_data.get('password', instance.password)
        instance.permission = validated_data.get('permission', instance.permission)
        instance.logged_in = validated_data.get('logged_in', instance.logged_in)
        instance.mean_rate = validated_data.get('mean_rate', instance.mean_rate)




class AnnouncementSerializer(serializers.ModelSerializer):
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
