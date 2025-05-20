package com.example.gps;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, JoystickView.JoystickListener {

    private GoogleMap mMap;
    private Marker movingMarker;
    private JoystickView joystick;
    private final Handler handler = new Handler();

    private LatLng startPoint;
    private LatLng endPoint;
    private boolean waitingForStart = true;

    // TODO: Replace with a safer way to manage API Key (e.g., local.properties, BuildConfig)
    private final String API_KEY = "AIzaSyC9LChRd5Yjy2HEZYlqaKlgnSLXT9pXHH8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        joystick = findViewById(R.id.joystick);
        joystick.setVisibility(View.GONE);

        Button toggleBtn = findViewById(R.id.btnToggleJoystick);
        toggleBtn.setOnClickListener(v -> {
            joystick.setVisibility(joystick.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 대한민국 초기 위치
        LatLng korea = new LatLng(37.5665, 126.9780);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(korea, 10));

        mMap.setOnMapClickListener(latLng -> {
            if (waitingForStart) {
                startPoint = latLng;
                // 기존 마커들 제거 (선택 사항, 필요에 따라 유지 가능)
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("출발 지점")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));

                if (movingMarker != null) movingMarker.remove();
                movingMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("현재 위치")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                waitingForStart = false;
            } else {
                endPoint = latLng;
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("도착 지점")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_end)));
                waitingForStart = true;
                fetchDirectionsAndMove();
            }
        });
    }

    private void fetchDirectionsAndMove() {
        new Thread(() -> {
            try {
                String urlStr = "https://maps.googleapis.com/maps/api/directions/json?origin="
                        + startPoint.latitude + "," + startPoint.longitude
                        + "&destination=" + endPoint.latitude + "," + endPoint.longitude
                        + "&mode=walking&key=" + API_KEY;

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    JSONObject json = new JSONObject(sb.toString());

                    // Enhanced: Extract detailed polyline from steps
                    List<LatLng> pathPoints = new ArrayList<>();
                    if (json.has("routes")) {
                        JSONArray routes = json.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            if (route.has("legs")) {
                                JSONArray legs = route.getJSONArray("legs");
                                if (legs.length() > 0) {
                                    JSONObject leg = legs.getJSONObject(0);
                                    if (leg.has("steps")) {
                                        JSONArray steps = leg.getJSONArray("steps");
                                        for (int i = 0; i < steps.length(); i++) {
                                            JSONObject step = steps.getJSONObject(i);
                                            if (step.has("polyline")) {
                                                String encodedStepPolyline = step.getJSONObject("polyline").getString("points");
                                                List<LatLng> stepPoints = PolylineDecoder.decode(encodedStepPolyline);
                                                pathPoints.addAll(stepPoints);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    Log.d("POLYLINE", "경로 좌표 수: " + pathPoints.size());

                    runOnUiThread(() -> {
                        if (pathPoints.isEmpty()) {
                            Log.e("POLYLINE", "경로 데이터가 비어있음 또는 가져오기 실패");
                            // TODO: 사용자에게 경로를 찾을 수 없음을 알리는 메시지 표시
                            return;
                        }
                        Log.d("POLYLINE", "moveAlongPath 호출됨");
                        moveAlongPath(pathPoints);
                    });
                } else {
                    Log.e("DIRECTIONS_API", "HTTP 오류 응답 코드: " + responseCode);
                    // TODO: 사용자에게 오류 발생을 알리는 메시지 표시
                }


            } catch (Exception e) {
                Log.e("DIRECTIONS_API", "오류 발생: " + e.getMessage());
                e.printStackTrace();
                runOnUiThread(() -> {
                    // TODO: 사용자에게 오류 발생을 알리는 메시지 표시
                });
            }
        }).start();
    }

    private void moveAlongPath(List<LatLng> path) {
        if (path == null || path.isEmpty() || movingMarker == null) {
            return;
        }

        // 마커 이동 애니메이션 지속 시간 (경로 전체에 대한 총 시간)
        // 이 값을 조정하여 이동 속도를 조절할 수 있습니다.
        // 예시: 경로의 길이에 비례하여 시간을 설정하거나 고정된 시간 사용
        long totalDuration = 10000; // 예시로 10초 설정

        // 첫 번째 지점으로 즉시 이동
        movingMarker.setPosition(path.get(0));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(path.get(0), 17)); // 첫 지점으로 카메라 이동

        // ValueAnimator를 사용하여 전체 경로를 따라 마커 부드럽게 이동
        ValueAnimator animator = ValueAnimator.ofInt(0, path.size() - 1);
        animator.setDuration(totalDuration); // 전체 애니메이션 시간
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // 또는 원하는 Interpolator 사용

        animator.addUpdateListener(animation -> {
            int index = (int) animation.getAnimatedValue();
            if (index < path.size() - 1) {
                // 현재 인덱스와 다음 인덱스 사이의 보간 계산
                float fraction = animation.getAnimatedFraction(); // 0에서 1까지의 애니메이션 진행률
                LatLng start = path.get(index);
                LatLng end = path.get(index + 1);

                // 선형 보간 (Linear Interpolation)
                double lat = start.latitude + (end.latitude - start.latitude) * (fraction * (path.size() - 1) - index);
                double lng = start.longitude + (end.longitude - start.longitude) * (fraction * (path.size() - 1) - index);

                LatLng interpolatedLatLng = new LatLng(lat, lng);
                movingMarker.setPosition(interpolatedLatLng);
                // 필요하다면 카메라를 마커 위치로 이동
                mMap.moveCamera(CameraUpdateFactory.newLatLng(interpolatedLatLng));
            } else if (index == path.size() - 1) {
                // 애니메이션 마지막 지점에 도달
                movingMarker.setPosition(path.get(path.size() - 1));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(path.get(path.size() - 1)));
            }
        });

        animator.start();
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent) {
        // 조이스틱 움직임에 따른 마커 이동 로직 구현 (선택 사항)
        // 여기서는 경로 따라 이동 애니메이션과 조이스틱 조작을 동시에 사용할 경우 충돌이 발생할 수 있습니다.
        // 필요에 따라 둘 중 하나만 활성화하거나, 조이스틱 조작 시 애니메이션을 중지하고 수동 조작 모드로 전환하는 로직을 추가해야 합니다.
        Log.d("Joystick", "X: " + xPercent + ", Y: " + yPercent);
        // 예시: 조이스틱으로 마커를 이동시키는 로직 (경로 이동과 함께 사용 시 로직 수정 필요)
        if (movingMarker != null) {
            LatLng currentPosition = movingMarker.getPosition();
            double currentLat = currentPosition.latitude;
            double currentLng = currentPosition.longitude;

            // 조이스틱 움직임에 따라 위치 변경량 계산 (조절 필요)
            double deltaLat = yPercent * 0.0001; // 위도 변경량 (조이스틱 상하)
            double deltaLng = xPercent * 0.0001; // 경도 변경량 (조이스틱 좌우)

            LatLng newPosition = new LatLng(currentLat + deltaLat, currentLng + deltaLng);
            movingMarker.setPosition(newPosition);
            // 필요하다면 카메라를 마커 위치로 이동
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
        }
    }

    // Polyline 디코딩 유틸리티 클래스 (이전에 제공된 코드를 기반으로 함)
    private static class PolylineDecoder {
        /**
         * Decodes an encoded path string into a list of LatLngs.
         * from https://developers.google.com/maps/documentation/utilities/library
         */
        static List<LatLng> decode(final String encodedPath) {
            int len = encodedPath.length();

            final List<LatLng> path = new ArrayList<LatLng>();
            int index = 0;
            int lat = 0;
            int lng = 0;

            while (index < len) {
                int result = 1;
                int shift = 0;
                int b;
                do {
                    b = encodedPath.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

                result = 1;
                shift = 0;
                do {
                    b = encodedPath.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

                path.add(new LatLng(lat * 1e-5, lng * 1e-5));
            }

            return path;
        }
    }
}