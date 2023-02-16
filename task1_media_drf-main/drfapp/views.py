from rest_framework.generics import DestroyAPIView
from rest_framework.response import Response
from .serializers import *
from .models import *
from rest_framework.views import APIView
from rest_framework import status
from django.shortcuts import get_object_or_404


class DelExampleAPIView(DestroyAPIView):
    queryset = ExampleModel.objects.all()
    serializer_class = ExampleSerializer


class ExampleApiView(APIView):
    def get(self, request, format=None):
        snippets = self.get_queryset(request)
        serializer = ExampleSerializer(snippets, many=True)
        return Response(serializer.data)

    def get_queryset(self, request):
        id = request.query_params.get('id')

        if id is not None:
            return ExampleModel.objects.filter(pk=id)
        else:
            return ExampleModel.objects.all()

    def post(self, request, format=None):
        serializer = ExampleSerializer(data=request.data)
        make_url = f"http://localhost:8009/media/img/{str(request.FILES.get('img'))}"

        if serializer.is_valid():
            serializer.save(file_url=make_url)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, format=None):
        url = request.query_params.get('url')
        assigned = request.query_params.get('assigned')
        if url and assigned is not None:
            post = get_object_or_404(ExampleModel, file_url=url)
            if post:
                if assigned == 'true':
                    assigned = True
                    post.save()
                elif assigned == 'false':
                    assigned = False

                post.assigned = assigned
                post.save()
                serializer = ExampleSerializer(post)
                return Response(serializer.data, status=status.HTTP_200_OK)
            return Response({'detail': 'Incorrect url'})
        else:
            return Response({"detail": "Object doesn't exists"}, status=status.HTTP_400_BAD_REQUEST)

